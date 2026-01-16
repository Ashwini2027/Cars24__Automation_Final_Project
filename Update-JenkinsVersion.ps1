<#
    .Synopsis
    Upgrades the version information in the register from the current Jenkins war file.
    .Description
    The purpose of this script is to update the version of Jenkins in the registry
    when the user may have upgraded the war file in place. The script probes the
    registry for information about the Jenkins install (path to war, etc.) and 
    then grabs the version information from the war to update the values in the
    registry so they match the version of the war file. 

    This will help with security scanners that look in the registry for versions
    of software and flag things when they are too low. The information in the 
    registry may be very old compared to what version of the war file is 
    actually installed on the system.
#>


# Self-elevate the script if required
if (-Not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] 'Administrator')) {
    # We may be running under powershell.exe or pwsh.exe, make sure we relaunch the same one.
    $Executable = [System.Diagnostics.Process]::GetCurrentProcess().MainModule.FileName
    if ([int](Get-CimInstance -Class Win32_OperatingSystem | Select-Object -ExpandProperty BuildNumber) -ge 6000) {
        # Launching with RunAs to get elevation
        $CommandLine = "-File `"" + $MyInvocation.MyCommand.Path + "`" " + $MyInvocation.UnboundArguments
        Start-Process -FilePath $Executable -Verb Runas -ArgumentList $CommandLine
        Exit
    }
}

function New-TemporaryDirectory {
    $Parent = [System.IO.Path]::GetTempPath()
    do {
        $Name = [System.IO.Path]::GetRandomFileName()
        $Item = New-Item -Path $Parent -Name $Name -ItemType "Directory" -ErrorAction SilentlyContinue
    } while (-not $Item)
    return $Item.FullName
}

function Exit-Script($Message, $Fatal = $False) {
    $ExitCode = 0
    if($Fatal) {
        Write-Error $Message
    } else {
        Write-Host $Message
    }
    Read-Host "Press ENTER to continue"
    Exit $ExitCode
}

# Let's find the location of the war file...
$JenkinsDir = Get-ItemPropertyValue -Path HKLM:\Software\Jenkins\InstalledProducts\Jenkins -Name InstallLocation -ErrorAction SilentlyContinue

if (($Null -eq $JenkinsDir) -or [String]::IsNullOrWhiteSpace($JenkinsDir)) {
    Exit-Script -Message "Jenkins does not seem to be installed. Please verify you have previously installed using the MSI installer" -Fatal $True
}

$WarPath = Join-Path $JenkinsDir "jenkins.war"
if(-Not (Test-Path $WarPath)) {
    Exit-Script -Message "Could not find war file at location found in registry, please verify Jenkins installation" -Fatal $True
}

# Get the MANIFEST.MF file from the war file to get the version of Jenkins
$TempWorkDir = New-TemporaryDirectory
$ManifestFile = Join-Path $TempWorkDir "MANIFEST.MF"
$Zip = [IO.Compression.ZipFile]::OpenRead($WarPath)
$Zip.Entries | Where-Object { $_.Name -like "MANIFEST.MF" } | ForEach-Object { [System.IO.Compression.ZipFileExtensions]::ExtractToFile($_, $ManiFestFile, $True) }
$Zip.Dispose()

$JenkinsVersion = $(Get-Content $ManiFestFile | Select-String -Pattern "^Jenkins-Version:\s*(.*)" | ForEach-Object { $_.Matches } | ForEach-Object { $_.Groups[1].Value } | Select-Object -First 1)
Remove-Item -Path $ManifestFile

# Convert the Jenkins version into what should be in the registry
$VersionItems = $JenkinsVersion.Split(".") | ForEach-Object { [int]::Parse($_) }

# Use the same encoding algorithm as the installer to encode the version into the correct format 
$RegistryEncodedVersion = 0
$Major = $VersionItems[0]
if ($VersionItems.Length -le 2) {
    $Minor = 0
    if (($VersionItems.Length -gt 1) -and ($VersionItems[1] -gt 255)) {
        $Minor = $VersionItems[1]
        $RegistryEncodedVersion = $RegistryEncodedVersion -bor ((($Major -band 0xff) -shl 24) -bor 0x00ff0000 -bor (($Minor * 10) -band 0x0000ffff))
    }
    else {
        $RegistryEncodedVersion = $RegistryEncodedVersion -bor (($Major -band 0xff) -shl 24)
    }
}
else {
    $Minor = $VersionItems[1]
    if ($Minor -gt 255) {
        $RegistryEncodedVersion = $RegistryEncodedVersion -bor ((($Major -band 0xff) -shl 24) -bor 0x00ff0000 -bor ((($Minor * 10) + $VersionItems[2]) -band 0x0000ffff))
    }
    else {
        $RegistryEncodedVersion = $RegistryEncodedVersion -bor ((($Major -band 0xff) -shl 24) -bor (($Minor -band 0xff) -shl 16) -bor ($VersionItems[2] -band 0x0000ffff))
    }
}

$ProductName = "Jenkins $JenkinsVersion"

# Find the registry key for Jenkins in the Installer\Products area and CurrentVersion\Uninstall
$JenkinsProductsRegistryKey = Get-ChildItem -Path HKLM:\SOFTWARE\Classes\Installer\Products  | Where-Object { $_.GetValue("ProductName", "").StartsWith("Jenkins") }

$JenkinsUninstallRegistryKey = Get-ChildItem -Path HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall  | Where-Object { $_.GetValue("DisplayName", "").StartsWith("Jenkins") }

if (($Null -eq $JenkinsProductsRegistryKey) -or ($Null -eq $JenkinsUninstallRegistryKey)) {
    Exit-Script -Message "Could not find the product information for Jenkins" -Fatal $True
}

# Update the Installer\Products area
$RegistryPath = $JenkinsProductsRegistryKey.Name.Substring($JenkinsProductsRegistryKey.Name.IndexOf("\"))

$OldProductName = $JenkinsProductsRegistryKey.GetValue("ProductName", "")
if ($OldProductName -ne $ProductName) {
    Set-ItemProperty -Path HKLM:$RegistryPath -Name "ProductName" -Type String -Value $ProductName 
}

$OldVersion = $JenkinsProductsRegistryKey.GetValue("Version", 0)
if ($OldVersion -ne $RegistryEncodedVersion) {
    Set-ItemProperty -Path HKLM:$RegistryPath -Name "Version" -Type DWord -Value $RegistryEncodedVersion
}

# Update the Uninstall area
$RegistryPath = $JenkinsUninstallRegistryKey.Name.Substring($JenkinsUninstallRegistryKey.Name.IndexOf("\"))
$OldDisplayName = $JenkinsUninstallRegistryKey.GetValue("DisplayName", "")
if ($OldDisplayName -ne $ProductName) {
    Set-ItemProperty -Path HKLM:$RegistryPath -Name "DisplayName" -Type String -Value $ProductName
}

$OldDisplayVersion = $JenkinsUninstallRegistryKey.GetValue("DisplayVersion", "")
$DisplayVersion = "{0}.{1}.{2}" -f ($RegistryEncodedVersion -shr 24), (($RegistryEncodedVersion -shr 16) -band 0xff), ($RegistryEncodedVersion -band 0xffff)
if ($OldDisplayVersion -ne $DisplayVersion) {
    Set-ItemProperty -Path HKLM:$RegistryPath -Name "DisplayVersion" -Type String -Value $DisplayVersion
}

$OldVersion = $JenkinsUninstallRegistryKey.GetValue("Version", 0)
if ($OldVersion -ne $RegistryEncodedVersion) {
    Set-ItemProperty -Path HKLM:$RegistryPath -Name "Version" -Type DWord -Value $RegistryEncodedVersion
}

$OldVersionMajor = $JenkinsUninstallRegistryKey.GetValue("VersionMajor", 0)
$VersionMajor = $RegistryEncodedVersion -shr 24
if ($OldVersionMajor -ne $VersionMajor) {

    Set-ItemProperty -Path HKLM:$RegistryPath -Name "VersionMajor" -Type DWord -Value $VersionMajor
}

$OldVersionMinor = $JenkinsUninstallRegistryKey.GetValue("VersionMinor", 0)
$VersionMinor = ($RegistryEncodedVersion -shr 16) -band 0xff
if ($OldVersionMinor -ne $VersionMinor) {
    Set-ItemProperty -Path HKLM:$RegistryPath -Name "VersionMinor" -Type DWord -Value $VersionMinor
}

Read-Host "Press ENTER to continue"

# SIG # Begin signature block
# MIIqAgYJKoZIhvcNAQcCoIIp8zCCKe8CAQExDzANBglghkgBZQMEAgEFADB5Bgor
# BgEEAYI3AgEEoGswaTA0BgorBgEEAYI3AgEeMCYCAwEAAAQQH8w7YFlLCE63JNLG
# KX7zUQIBAAIBAAIBAAIBAAIBADAxMA0GCWCGSAFlAwQCAQUABCDbyeWzg9oNvcU/
# j3Q0982rRxp4werHyv0Gv0YLxmkVkaCCDlowggawMIIEmKADAgECAhAIrUCyYNKc
# TJ9ezam9k67ZMA0GCSqGSIb3DQEBDAUAMGIxCzAJBgNVBAYTAlVTMRUwEwYDVQQK
# EwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5jb20xITAfBgNV
# BAMTGERpZ2lDZXJ0IFRydXN0ZWQgUm9vdCBHNDAeFw0yMTA0MjkwMDAwMDBaFw0z
# NjA0MjgyMzU5NTlaMGkxCzAJBgNVBAYTAlVTMRcwFQYDVQQKEw5EaWdpQ2VydCwg
# SW5jLjFBMD8GA1UEAxM4RGlnaUNlcnQgVHJ1c3RlZCBHNCBDb2RlIFNpZ25pbmcg
# UlNBNDA5NiBTSEEzODQgMjAyMSBDQTEwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAw
# ggIKAoICAQDVtC9C0CiteLdd1TlZG7GIQvUzjOs9gZdwxbvEhSYwn6SOaNhc9es0
# JAfhS0/TeEP0F9ce2vnS1WcaUk8OoVf8iJnBkcyBAz5NcCRks43iCH00fUyAVxJr
# Q5qZ8sU7H/Lvy0daE6ZMswEgJfMQ04uy+wjwiuCdCcBlp/qYgEk1hz1RGeiQIXhF
# LqGfLOEYwhrMxe6TSXBCMo/7xuoc82VokaJNTIIRSFJo3hC9FFdd6BgTZcV/sk+F
# LEikVoQ11vkunKoAFdE3/hoGlMJ8yOobMubKwvSnowMOdKWvObarYBLj6Na59zHh
# 3K3kGKDYwSNHR7OhD26jq22YBoMbt2pnLdK9RBqSEIGPsDsJ18ebMlrC/2pgVItJ
# wZPt4bRc4G/rJvmM1bL5OBDm6s6R9b7T+2+TYTRcvJNFKIM2KmYoX7BzzosmJQay
# g9Rc9hUZTO1i4F4z8ujo7AqnsAMrkbI2eb73rQgedaZlzLvjSFDzd5Ea/ttQokbI
# YViY9XwCFjyDKK05huzUtw1T0PhH5nUwjewwk3YUpltLXXRhTT8SkXbev1jLchAp
# QfDVxW0mdmgRQRNYmtwmKwH0iU1Z23jPgUo+QEdfyYFQc4UQIyFZYIpkVMHMIRro
# OBl8ZhzNeDhFMJlP/2NPTLuqDQhTQXxYPUez+rbsjDIJAsxsPAxWEQIDAQABo4IB
# WTCCAVUwEgYDVR0TAQH/BAgwBgEB/wIBADAdBgNVHQ4EFgQUaDfg67Y7+F8Rhvv+
# YXsIiGX0TkIwHwYDVR0jBBgwFoAU7NfjgtJxXWRM3y5nP+e6mK4cD08wDgYDVR0P
# AQH/BAQDAgGGMBMGA1UdJQQMMAoGCCsGAQUFBwMDMHcGCCsGAQUFBwEBBGswaTAk
# BggrBgEFBQcwAYYYaHR0cDovL29jc3AuZGlnaWNlcnQuY29tMEEGCCsGAQUFBzAC
# hjVodHRwOi8vY2FjZXJ0cy5kaWdpY2VydC5jb20vRGlnaUNlcnRUcnVzdGVkUm9v
# dEc0LmNydDBDBgNVHR8EPDA6MDigNqA0hjJodHRwOi8vY3JsMy5kaWdpY2VydC5j
# b20vRGlnaUNlcnRUcnVzdGVkUm9vdEc0LmNybDAcBgNVHSAEFTATMAcGBWeBDAED
# MAgGBmeBDAEEATANBgkqhkiG9w0BAQwFAAOCAgEAOiNEPY0Idu6PvDqZ01bgAhql
# +Eg08yy25nRm95RysQDKr2wwJxMSnpBEn0v9nqN8JtU3vDpdSG2V1T9J9Ce7FoFF
# UP2cvbaF4HZ+N3HLIvdaqpDP9ZNq4+sg0dVQeYiaiorBtr2hSBh+3NiAGhEZGM1h
# mYFW9snjdufE5BtfQ/g+lP92OT2e1JnPSt0o618moZVYSNUa/tcnP/2Q0XaG3Ryw
# YFzzDaju4ImhvTnhOE7abrs2nfvlIVNaw8rpavGiPttDuDPITzgUkpn13c5Ubdld
# AhQfQDN8A+KVssIhdXNSy0bYxDQcoqVLjc1vdjcshT8azibpGL6QB7BDf5WIIIJw
# 8MzK7/0pNVwfiThV9zeKiwmhywvpMRr/LhlcOXHhvpynCgbWJme3kuZOX956rEnP
# LqR0kq3bPKSchh/jwVYbKyP/j7XqiHtwa+aguv06P0WmxOgWkVKLQcBIhEuWTatE
# QOON8BUozu3xGFYHKi8QxAwIZDwzj64ojDzLj4gLDb879M4ee47vtevLt/B3E+bn
# KD+sEq6lLyJsQfmCXBVmzGwOysWGw/YmMwwHS6DTBwJqakAwSEs0qFEgu60bhQji
# WQ1tygVQK+pKHJ6l/aCnHwZ05/LWUpD9r4VIIflXO7ScA+2GRfS0YW6/aOImYIbq
# yK+p/pQd52MbOoZWeE4wggeiMIIFiqADAgECAhADJPGbkeizM3ep7tjv4Oh/MA0G
# CSqGSIb3DQEBCwUAMGkxCzAJBgNVBAYTAlVTMRcwFQYDVQQKEw5EaWdpQ2VydCwg
# SW5jLjFBMD8GA1UEAxM4RGlnaUNlcnQgVHJ1c3RlZCBHNCBDb2RlIFNpZ25pbmcg
# UlNBNDA5NiBTSEEzODQgMjAyMSBDQTEwHhcNMjMwNDAzMDAwMDAwWhcNMjYwNTE2
# MjM1OTU5WjCBqTELMAkGA1UEBhMCVVMxETAPBgNVBAgTCERlbGF3YXJlMRMwEQYD
# VQQHEwpXaWxtaW5ndG9uMTgwNgYDVQQKEy9DREYgQmluYXJ5IFByb2plY3QgYSBT
# ZXJpZXMgb2YgTEYgUHJvamVjdHMsIExMQzE4MDYGA1UEAxMvQ0RGIEJpbmFyeSBQ
# cm9qZWN0IGEgU2VyaWVzIG9mIExGIFByb2plY3RzLCBMTEMwggIiMA0GCSqGSIb3
# DQEBAQUAA4ICDwAwggIKAoICAQDfqgZcXDJTB5793QlJS7n18mEi24oIQM8oBEYa
# 9swJt4M/pvIyWSSKj0FIKtqOzJQAlaf1cyxOlAisOmsc6K1CCFnnFKvIlyNjRCso
# uoanpbp2Tm0YeoLZhnb71IgWKxcI0Rwida9L+sAsHvsmhWjBQiIs0iAn566nk5UM
# tucGtA4IIK516JmHP8oJxxTgB1X7epupLf0InZeCzd+p36Ct77aCh/wXAnimeBl+
# GrZ+fzHZLCxl7BYk5USiRHVAPJ/nyhqJuOdkHToplFApJBYQYAOhve4S8HWmyqKt
# oBCzeSOQPRYCLQ2bYAo/C23ldMEzEVXd1hju59ZpR4cbJOI4Uhh9tGy0NuzSGhf0
# QdG2XEFdPux/+JW47xpfe4IEkYUq3AKIaZVKWmCZQNoBNrwEmnccYp4tBCsGWO4E
# gcp6V9uChgFpOU4d22hcOxlJjJcTMduqBIskgpoZgoL8RuFXk1P3s9LzROzgJO4F
# d2GljWwDRlut5w/eUuo+++gPmawSKN7FvjvMG3DJGVFBOphwrAGGw7BQ7zSThICJ
# F7kuEFsawCdFNScZSll7FC011U6Hf/6qy/w+lEFhEPFc9GmHO2eQlD/EiU3flXex
# 3qsT0Tagv74AwJHK8Jh6E/WRa2Skqaj3IcPIkm6aZbSPGGNujjXh1KfOGq8hZ/0K
# MUQM2wIDAQABo4ICAzCCAf8wHwYDVR0jBBgwFoAUaDfg67Y7+F8Rhvv+YXsIiGX0
# TkIwHQYDVR0OBBYEFEA4cBGhmjuHUVaxlLPntLnIMLmQMA4GA1UdDwEB/wQEAwIH
# gDATBgNVHSUEDDAKBggrBgEFBQcDAzCBtQYDVR0fBIGtMIGqMFOgUaBPhk1odHRw
# Oi8vY3JsMy5kaWdpY2VydC5jb20vRGlnaUNlcnRUcnVzdGVkRzRDb2RlU2lnbmlu
# Z1JTQTQwOTZTSEEzODQyMDIxQ0ExLmNybDBToFGgT4ZNaHR0cDovL2NybDQuZGln
# aWNlcnQuY29tL0RpZ2lDZXJ0VHJ1c3RlZEc0Q29kZVNpZ25pbmdSU0E0MDk2U0hB
# Mzg0MjAyMUNBMS5jcmwwPgYDVR0gBDcwNTAzBgZngQwBBAEwKTAnBggrBgEFBQcC
# ARYbaHR0cDovL3d3dy5kaWdpY2VydC5jb20vQ1BTMIGUBggrBgEFBQcBAQSBhzCB
# hDAkBggrBgEFBQcwAYYYaHR0cDovL29jc3AuZGlnaWNlcnQuY29tMFwGCCsGAQUF
# BzAChlBodHRwOi8vY2FjZXJ0cy5kaWdpY2VydC5jb20vRGlnaUNlcnRUcnVzdGVk
# RzRDb2RlU2lnbmluZ1JTQTQwOTZTSEEzODQyMDIxQ0ExLmNydDAJBgNVHRMEAjAA
# MA0GCSqGSIb3DQEBCwUAA4ICAQA0nYhrG8FM2LQT4e18lk9EgwvuN4ic4A87ci4b
# cxSjYmuUtM2xtsq/9mYROa+7054SbvE2JKyqkvisIb/Ks9zhTSr6hMN0PTO1fKjf
# tth5vBOc7JZTZEsMRJrjZN+zmE6M0w7R67r0TVKbOBWJeUH5g/XMOPaWH8WEEF5S
# m8f2QjmFYyi9inBD5EWBuGK9q4lfda2k2hZ5AY2IddA7apZTiD9QQH3ex/biVVr2
# Zql8TC8918EDnBTwntySMtPLP+GCp416JrQGyapolwbHRDug+hQQJ7+ie8ygWr6K
# 7aAOpvleE/Wjqkl023x6djUdMDe/MbqRDzkOU83osgN9sySIEzTPj5sH+BEjOjNo
# 5jkcPMIvLMeudoweglm+llsnnJMQNLKjik6vp0Klvc3Hphs0Iqo4oEixf5QGA1Ja
# BGsu/nBx94qGJg7zPmCDkTVR/kpbCywrpCnq5CDPMQjR8TkadzG9OUR/nr+YXDX8
# lfzH7MRxoh3dEOh10wduINeGf6FHJhNVcrf4Mts5oLFXLbKTZTPeJ+Vni2BVNOIA
# roQvMFYjIx8YZY0Z2n2xxtePSPh8fPkgH+eeAO/zvZEX0dIz+FudOpsrhO7MTrGl
# XZ5roSeSVy+ZqVngRkJaMCtsf0rd/4uRfxjnsdWWMaiBH4vDa/uI+JjwaPGMRH9C
# +U9y3DGCGv4wghr6AgEBMH0waTELMAkGA1UEBhMCVVMxFzAVBgNVBAoTDkRpZ2lD
# ZXJ0LCBJbmMuMUEwPwYDVQQDEzhEaWdpQ2VydCBUcnVzdGVkIEc0IENvZGUgU2ln
# bmluZyBSU0E0MDk2IFNIQTM4NCAyMDIxIENBMQIQAyTxm5HoszN3qe7Y7+DofzAN
# BglghkgBZQMEAgEFAKCB2DAZBgkqhkiG9w0BCQMxDAYKKwYBBAGCNwIBBDAcBgor
# BgEEAYI3AgELMQ4wDAYKKwYBBAGCNwIBFTAvBgkqhkiG9w0BCQQxIgQgSryF4dcs
# 7+G2EseArPHZ/z/KN04sbkO7SZGsfdIrWUgwbAYKKwYBBAGCNwIBDDFeMFygRIBC
# AEoAZQBuAGsAaQBuAHMAIABBAHUAdABvAG0AYQB0AGkAbwBuACAAUwBlAHIAdgBl
# AHIAIAAyAC4ANQAyADgALgAyoRSAEmh0dHBzOi8vamVua2lucy5pbzANBgkqhkiG
# 9w0BAQEFAASCAgDWNZtgKh44+GCZd/dng4v+q8TKVUPG3gWwG1hXYEQhWOKoLzfq
# 4TDCuWOQl3xMRXemE9jTiNKvLB3CBNoi68vS1feBC/70xHF1T3hlh9EcNRyD6kex
# sjD5hdTN6z6yeqE43v6m6pDeqzUB/cSV71GkuHhwZKOtdmY6UHWPVVdOEtc+yJv1
# khbz2wSZ5lHyXyUlPo3pIKllBjkNMsLPGPKnOpFte4Ut54ZD9kut5OKQL/zfnzVu
# /vt32pF1+9vP6FP+93B51ZzaJZe4V3AqPqOKz99bjz4XAKCXbm/8mTjc+tCjTf0r
# JIGQVL4qKqrvaJJP8aN7XuQVuS+kJtJZTJnLi0jn/u+ZE1IWsSrieWqgeD2M9hxB
# SUP7dJaX5fll57rGBfqHv3QYzHEJUhW+jk0YMX/SguaFk3mFM05xQJh6yYTs3cwh
# WE6D9qU2Tu0Yw/k6O/DXr76BQKBZaAaPFrioZZeI4ZqxuHtm/ANdaqo5XxoAbXRq
# SEhY1GcaIRfvHvCGqLacRrdPxv94yj3/oSBSVQ8T6ibaWDKfD4MFjMdrT1Q+w0WF
# RLb668lp89cBEe75IKYYONUjx4R5Au+DYLJTFaXnajrU9tWMcHeKjwXyTItY8WOJ
# bjInLA0+B9z2Szewew3IHd08JBNxwtHtW5Tc1sRb76J2E0iHcugXj7MsD6GCF3cw
# ghdzBgorBgEEAYI3AwMBMYIXYzCCF18GCSqGSIb3DQEHAqCCF1AwghdMAgEDMQ8w
# DQYJYIZIAWUDBAIBBQAweAYLKoZIhvcNAQkQAQSgaQRnMGUCAQEGCWCGSAGG/WwH
# ATAxMA0GCWCGSAFlAwQCAQUABCAo6NrzMlPcpyeeG5pPHf43+67GJyPVViJil4AQ
# k8jEJAIRALcVprxqrZ1utW97zNLAEqYYDzIwMjUxMTEyMTUyMDExWqCCEzowggbt
# MIIE1aADAgECAhAKgO8YS43xBYLRxHanlXRoMA0GCSqGSIb3DQEBCwUAMGkxCzAJ
# BgNVBAYTAlVTMRcwFQYDVQQKEw5EaWdpQ2VydCwgSW5jLjFBMD8GA1UEAxM4RGln
# aUNlcnQgVHJ1c3RlZCBHNCBUaW1lU3RhbXBpbmcgUlNBNDA5NiBTSEEyNTYgMjAy
# NSBDQTEwHhcNMjUwNjA0MDAwMDAwWhcNMzYwOTAzMjM1OTU5WjBjMQswCQYDVQQG
# EwJVUzEXMBUGA1UEChMORGlnaUNlcnQsIEluYy4xOzA5BgNVBAMTMkRpZ2lDZXJ0
# IFNIQTI1NiBSU0E0MDk2IFRpbWVzdGFtcCBSZXNwb25kZXIgMjAyNSAxMIICIjAN
# BgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA0EasLRLGntDqrmBWsytXum9R/4Zw
# CgHfyjfMGUIwYzKomd8U1nH7C8Dr0cVMF3BsfAFI54um8+dnxk36+jx0Tb+k+87H
# 9WPxNyFPJIDZHhAqlUPt281mHrBbZHqRK71Em3/hCGC5KyyneqiZ7syvFXJ9A72w
# zHpkBaMUNg7MOLxI6E9RaUueHTQKWXymOtRwJXcrcTTPPT2V1D/+cFllESviH8Yj
# oPFvZSjKs3SKO1QNUdFd2adw44wDcKgH+JRJE5Qg0NP3yiSyi5MxgU6cehGHr7zo
# u1znOM8odbkqoK+lJ25LCHBSai25CFyD23DZgPfDrJJJK77epTwMP6eKA0kWa3os
# Ae8fcpK40uhktzUd/Yk0xUvhDU6lvJukx7jphx40DQt82yepyekl4i0r8OEps/FN
# O4ahfvAk12hE5FVs9HVVWcO5J4dVmVzix4A77p3awLbr89A90/nWGjXMGn7FQhmS
# lIUDy9Z2hSgctaepZTd0ILIUbWuhKuAeNIeWrzHKYueMJtItnj2Q+aTyLLKLM0Mh
# eP/9w6CtjuuVHJOVoIJ/DtpJRE7Ce7vMRHoRon4CWIvuiNN1Lk9Y+xZ66lazs2kK
# FSTnnkrT3pXWETTJkhd76CIDBbTRofOsNyEhzZtCGmnQigpFHti58CSmvEyJcAlD
# VcKacJ+A9/z7eacCAwEAAaOCAZUwggGRMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYE
# FOQ7/PIx7f391/ORcWMZUEPPYYzoMB8GA1UdIwQYMBaAFO9vU0rp5AZ8esrikFb2
# L9RJ7MtOMA4GA1UdDwEB/wQEAwIHgDAWBgNVHSUBAf8EDDAKBggrBgEFBQcDCDCB
# lQYIKwYBBQUHAQEEgYgwgYUwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2lj
# ZXJ0LmNvbTBdBggrBgEFBQcwAoZRaHR0cDovL2NhY2VydHMuZGlnaWNlcnQuY29t
# L0RpZ2lDZXJ0VHJ1c3RlZEc0VGltZVN0YW1waW5nUlNBNDA5NlNIQTI1NjIwMjVD
# QTEuY3J0MF8GA1UdHwRYMFYwVKBSoFCGTmh0dHA6Ly9jcmwzLmRpZ2ljZXJ0LmNv
# bS9EaWdpQ2VydFRydXN0ZWRHNFRpbWVTdGFtcGluZ1JTQTQwOTZTSEEyNTYyMDI1
# Q0ExLmNybDAgBgNVHSAEGTAXMAgGBmeBDAEEAjALBglghkgBhv1sBwEwDQYJKoZI
# hvcNAQELBQADggIBAGUqrfEcJwS5rmBB7NEIRJ5jQHIh+OT2Ik/bNYulCrVvhREa
# fBYF0RkP2AGr181o2YWPoSHz9iZEN/FPsLSTwVQWo2H62yGBvg7ouCODwrx6ULj6
# hYKqdT8wv2UV+Kbz/3ImZlJ7YXwBD9R0oU62PtgxOao872bOySCILdBghQ/ZLcdC
# 8cbUUO75ZSpbh1oipOhcUT8lD8QAGB9lctZTTOJM3pHfKBAEcxQFoHlt2s9sXoxF
# izTeHihsQyfFg5fxUFEp7W42fNBVN4ueLaceRf9Cq9ec1v5iQMWTFQa0xNqItH3C
# PFTG7aEQJmmrJTV3Qhtfparz+BW60OiMEgV5GWoBy4RVPRwqxv7Mk0Sy4QHs7v9y
# 69NBqycz0BZwhB9WOfOu/CIJnzkQTwtSSpGGhLdjnQ4eBpjtP+XB3pQCtv4E5UCS
# Dag6+iX8MmB10nfldPF9SVD7weCC3yXZi/uuhqdwkgVxuiMFzGVFwYbQsiGnoa9F
# 5AaAyBjFBtXVLcKtapnMG3VH3EmAp/jsJ3FVF3+d1SVDTmjFjLbNFZUWMXuZyvgL
# fgyPehwJVxwC+UpX2MSey2ueIu9THFVkT+um1vshETaWyQo8gmBto/m3acaP9Qsu
# Lj3FNwFlTxq25+T4QwX9xa6ILs84ZPvmpovq90K8eWyG2N01c4IhSOxqt81nMIIG
# tDCCBJygAwIBAgIQDcesVwX/IZkuQEMiDDpJhjANBgkqhkiG9w0BAQsFADBiMQsw
# CQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3d3cu
# ZGlnaWNlcnQuY29tMSEwHwYDVQQDExhEaWdpQ2VydCBUcnVzdGVkIFJvb3QgRzQw
# HhcNMjUwNTA3MDAwMDAwWhcNMzgwMTE0MjM1OTU5WjBpMQswCQYDVQQGEwJVUzEX
# MBUGA1UEChMORGlnaUNlcnQsIEluYy4xQTA/BgNVBAMTOERpZ2lDZXJ0IFRydXN0
# ZWQgRzQgVGltZVN0YW1waW5nIFJTQTQwOTYgU0hBMjU2IDIwMjUgQ0ExMIICIjAN
# BgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtHgx0wqYQXK+PEbAHKx126NGaHS0
# URedTa2NDZS1mZaDLFTtQ2oRjzUXMmxCqvkbsDpz4aH+qbxeLho8I6jY3xL1IusL
# opuW2qftJYJaDNs1+JH7Z+QdSKWM06qchUP+AbdJgMQB3h2DZ0Mal5kYp77jYMVQ
# XSZH++0trj6Ao+xh/AS7sQRuQL37QXbDhAktVJMQbzIBHYJBYgzWIjk8eDrYhXDE
# pKk7RdoX0M980EpLtlrNyHw0Xm+nt5pnYJU3Gmq6bNMI1I7Gb5IBZK4ivbVCiZv7
# PNBYqHEpNVWC2ZQ8BbfnFRQVESYOszFI2Wv82wnJRfN20VRS3hpLgIR4hjzL0hpo
# YGk81coWJ+KdPvMvaB0WkE/2qHxJ0ucS638ZxqU14lDnki7CcoKCz6eum5A19WZQ
# HkqUJfdkDjHkccpL6uoG8pbF0LJAQQZxst7VvwDDjAmSFTUms+wV/FbWBqi7fTJn
# jq3hj0XbQcd8hjj/q8d6ylgxCZSKi17yVp2NL+cnT6Toy+rN+nM8M7LnLqCrO2JP
# 3oW//1sfuZDKiDEb1AQ8es9Xr/u6bDTnYCTKIsDq1BtmXUqEG1NqzJKS4kOmxkYp
# 2WyODi7vQTCBZtVFJfVZ3j7OgWmnhFr4yUozZtqgPrHRVHhGNKlYzyjlroPxul+b
# gIspzOwbtmsgY1MCAwEAAaOCAV0wggFZMBIGA1UdEwEB/wQIMAYBAf8CAQAwHQYD
# VR0OBBYEFO9vU0rp5AZ8esrikFb2L9RJ7MtOMB8GA1UdIwQYMBaAFOzX44LScV1k
# TN8uZz/nupiuHA9PMA4GA1UdDwEB/wQEAwIBhjATBgNVHSUEDDAKBggrBgEFBQcD
# CDB3BggrBgEFBQcBAQRrMGkwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2lj
# ZXJ0LmNvbTBBBggrBgEFBQcwAoY1aHR0cDovL2NhY2VydHMuZGlnaWNlcnQuY29t
# L0RpZ2lDZXJ0VHJ1c3RlZFJvb3RHNC5jcnQwQwYDVR0fBDwwOjA4oDagNIYyaHR0
# cDovL2NybDMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0VHJ1c3RlZFJvb3RHNC5jcmww
# IAYDVR0gBBkwFzAIBgZngQwBBAIwCwYJYIZIAYb9bAcBMA0GCSqGSIb3DQEBCwUA
# A4ICAQAXzvsWgBz+Bz0RdnEwvb4LyLU0pn/N0IfFiBowf0/Dm1wGc/Do7oVMY2mh
# XZXjDNJQa8j00DNqhCT3t+s8G0iP5kvN2n7Jd2E4/iEIUBO41P5F448rSYJ59Ib6
# 1eoalhnd6ywFLerycvZTAz40y8S4F3/a+Z1jEMK/DMm/axFSgoR8n6c3nuZB9BfB
# wAQYK9FHaoq2e26MHvVY9gCDA/JYsq7pGdogP8HRtrYfctSLANEBfHU16r3J05qX
# 3kId+ZOczgj5kjatVB+NdADVZKON/gnZruMvNYY2o1f4MXRJDMdTSlOLh0HCn2cQ
# LwQCqjFbqrXuvTPSegOOzr4EWj7PtspIHBldNE2K9i697cvaiIo2p61Ed2p8xMJb
# 82Yosn0z4y25xUbI7GIN/TpVfHIqQ6Ku/qjTY6hc3hsXMrS+U0yy+GWqAXam4ToW
# d2UQ1KYT70kZjE4YtL8Pbzg0c1ugMZyZZd/BdHLiRu7hAWE6bTEm4XYRkA6Tl4KS
# FLFk43esaUeqGkH/wyW4N7OigizwJWeukcyIPbAvjSabnf7+Pu0VrFgoiovRDiyx
# 3zEdmcif/sYQsfch28bZeUz2rtY/9TCA6TD8dC3JE3rYkrhLULy7Dc90G6e8Blqm
# yIjlgp2+VqsS9/wQD7yFylIz0scmbKvFoW2jNrbM1pD2T7m3XDCCBY0wggR1oAMC
# AQICEA6bGI750C3n79tQ4ghAGFowDQYJKoZIhvcNAQEMBQAwZTELMAkGA1UEBhMC
# VVMxFTATBgNVBAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3LmRpZ2ljZXJ0
# LmNvbTEkMCIGA1UEAxMbRGlnaUNlcnQgQXNzdXJlZCBJRCBSb290IENBMB4XDTIy
# MDgwMTAwMDAwMFoXDTMxMTEwOTIzNTk1OVowYjELMAkGA1UEBhMCVVMxFTATBgNV
# BAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3LmRpZ2ljZXJ0LmNvbTEhMB8G
# A1UEAxMYRGlnaUNlcnQgVHJ1c3RlZCBSb290IEc0MIICIjANBgkqhkiG9w0BAQEF
# AAOCAg8AMIICCgKCAgEAv+aQc2jeu+RdSjwwIjBpM+zCpyUuySE98orYWcLhKac9
# WKt2ms2uexuEDcQwH/MbpDgW61bGl20dq7J58soR0uRf1gU8Ug9SH8aeFaV+vp+p
# VxZZVXKvaJNwwrK6dZlqczKU0RBEEC7fgvMHhOZ0O21x4i0MG+4g1ckgHWMpLc7s
# Xk7Ik/ghYZs06wXGXuxbGrzryc/NrDRAX7F6Zu53yEioZldXn1RYjgwrt0+nMNlW
# 7sp7XeOtyU9e5TXnMcvak17cjo+A2raRmECQecN4x7axxLVqGDgDEI3Y1DekLgV9
# iPWCPhCRcKtVgkEy19sEcypukQF8IUzUvK4bA3VdeGbZOjFEmjNAvwjXWkmkwuap
# oGfdpCe8oU85tRFYF/ckXEaPZPfBaYh2mHY9WV1CdoeJl2l6SPDgohIbZpp0yt5L
# HucOY67m1O+SkjqePdwA5EUlibaaRBkrfsCUtNJhbesz2cXfSwQAzH0clcOP9yGy
# shG3u3/y1YxwLEFgqrFjGESVGnZifvaAsPvoZKYz0YkH4b235kOkGLimdwHhD5QM
# IR2yVCkliWzlDlJRR3S+Jqy2QXXeeqxfjT/JvNNBERJb5RBQ6zHFynIWIgnffEx1
# P2PsIV/EIFFrb7GrhotPwtZFX50g/KEexcCPorF+CiaZ9eRpL5gdLfXZqbId5RsC
# AwEAAaOCATowggE2MA8GA1UdEwEB/wQFMAMBAf8wHQYDVR0OBBYEFOzX44LScV1k
# TN8uZz/nupiuHA9PMB8GA1UdIwQYMBaAFEXroq/0ksuCMS1Ri6enIZ3zbcgPMA4G
# A1UdDwEB/wQEAwIBhjB5BggrBgEFBQcBAQRtMGswJAYIKwYBBQUHMAGGGGh0dHA6
# Ly9vY3NwLmRpZ2ljZXJ0LmNvbTBDBggrBgEFBQcwAoY3aHR0cDovL2NhY2VydHMu
# ZGlnaWNlcnQuY29tL0RpZ2lDZXJ0QXNzdXJlZElEUm9vdENBLmNydDBFBgNVHR8E
# PjA8MDqgOKA2hjRodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vRGlnaUNlcnRBc3N1
# cmVkSURSb290Q0EuY3JsMBEGA1UdIAQKMAgwBgYEVR0gADANBgkqhkiG9w0BAQwF
# AAOCAQEAcKC/Q1xV5zhfoKN0Gz22Ftf3v1cHvZqsoYcs7IVeqRq7IviHGmlUIu2k
# iHdtvRoU9BNKei8ttzjv9P+Aufih9/Jy3iS8UgPITtAq3votVs/59PesMHqai7Je
# 1M/RQ0SbQyHrlnKhSLSZy51PpwYDE3cnRNTnf+hZqPC/Lwum6fI0POz3A8eHqNJM
# QBk1RmppVLC4oVaO7KTVPeix3P0c2PR3WlxUjG/voVA9/HYJaISfb8rbII01YBwC
# A8sgsKxYoA5AY8WYIsGyWfVVa88nq2x2zm8jLfR+cWojayL/ErhULSd+2DrZ8LaH
# lv1b0VysGMNNn3O3AamfV6peKOK5lDGCA3wwggN4AgEBMH0waTELMAkGA1UEBhMC
# VVMxFzAVBgNVBAoTDkRpZ2lDZXJ0LCBJbmMuMUEwPwYDVQQDEzhEaWdpQ2VydCBU
# cnVzdGVkIEc0IFRpbWVTdGFtcGluZyBSU0E0MDk2IFNIQTI1NiAyMDI1IENBMQIQ
# CoDvGEuN8QWC0cR2p5V0aDANBglghkgBZQMEAgEFAKCB0TAaBgkqhkiG9w0BCQMx
# DQYLKoZIhvcNAQkQAQQwHAYJKoZIhvcNAQkFMQ8XDTI1MTExMjE1MjAxMVowKwYL
# KoZIhvcNAQkQAgwxHDAaMBgwFgQU3WIwrIYKLTBr2jixaHlSMAf7QX4wLwYJKoZI
# hvcNAQkEMSIEIAn584FJU3gKMEFWnAK99QZHF3h9RQaBtcjoG3ai4T5zMDcGCyqG
# SIb3DQEJEAIvMSgwJjAkMCIEIEqgP6Is11yExVyTj4KOZ2ucrsqzP+NtJpqjNPFG
# EQozMA0GCSqGSIb3DQEBAQUABIICAK+DGnmT6pIjMxRmsyGKDEH8Pcu5EMVobEjS
# DpiECoDmfHo/SCMt/FTSD+e26zXkUyL/lg30FugmY91i+rGYszNzUukUXTqZl6BJ
# 3Baa7TpE2zBq/8b+JZppVpUaxE0eMAq4Q37/ORIvhZvyjufPwBRHPE3afI3Xhutn
# 9tEJCEOI37Vh9dNmxnyJBBWGtpOZf9DzTufWFDPc4Dhf33P7F6KbOnmnr5xYEuaI
# 4OQXFgTZeX6U5NAxPOPYrgSS94bchjdGB6BkmPsj+MHgYBA7A2B0xv//zXSKoIcb
# fcMRGGceOuOvP//6NkKu3SbIW/irlERXqX4rQT7hWm7xpIWVSBIY0QH0SgrThOXP
# f3cFAoJY8/3c4jdqH1fSaTNGa3LRdezsjvb0cvxE4ZvC4EE55IGxObFkrNX8jqWE
# iIFI0gE2mJR+gUJd+CWrNSiH3h9dM/yMofVLIpd6fglvZB5zmAdJE23tEN0wvglE
# Btc2cGfF8+YmvQTulMnQsmuN8cfF6HQVPx1NE9py2xT1/nRffzMOTyo4pavZ5Zw8
# 5H/p/D5ceSjcmOpk0fDqbklVsFbOImUDHCMaivqs4VsP1jeF0Dz9ehsAFbbH+50x
# WQoPwy+mEinjEx/fpxKznuVKJDfuqoOZx+Ub808r2EaNqp+bxWCz9AnxhEqWJG9w
# NCerNDOr
# SIG # End signature block
