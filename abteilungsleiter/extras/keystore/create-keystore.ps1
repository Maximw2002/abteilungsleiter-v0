# Copyright (C) 2019 - present Juergen Zimmermann, Hochschule Karlsruhe
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

# "Param" muss in der 1. Zeile sein
Param (
    [string]$duration = '165'
)

Set-StrictMode -Version Latest

$versionMinimum = [Version]'7.4.0'
$versionCurrent = $PSVersionTable.PSVersion
if ($versionMinimum -gt $versionCurrent) {
    throw "PowerShell $versionMinimum statt $versionCurrent erforderlich"
}

# https://kubernetes.io/docs/concepts/services-networking/dns-pod-service
# https://kubernetes.io/docs/tasks/administer-cluster/certificates
# https://stackoverflow.com/questions/63736299/how-to-resolve-the-hostname-between-the-pods-in-the-kubernetes-cluster
# https://medium.com/avmconsulting-blog/how-to-secure-applications-on-kubernetes-ssl-tls-certificates-8f7f5751d788
$rechnername_1 = 'kunde'
$rechnername_2 = 'bestellung'
$rechnernamen = "DNS:$rechnername_1,DNS:$rechnername_1.acme.svc,DNS:$rechnername_1.acme.svc.cluster.local,DNS:$rechnername_2,DNS:$rechnername_2.acme.svc,DNS:$rechnername_2.acme.svc.cluster.local"

$alias = 'microservice'
$keystoreDir = '..\..\src\main\resources'
$certDir = '.'
$password = 'zimmermann'
$keySize = 2048
$email = 'Juergen.Zimmermann@h-ka.de'
$name = 'Juergen Zimmermann'

$ipIndex = $(Get-NetAdapter -Name 'Ethernet').ifIndex
$ip = $(Get-NetIPAddress -InterfaceIndex $ipIndex -AddressFamily IPv4).IPAddress
Write-Output "IP-Adresse: $ip"

Write-Output ''
Write-Output '--------------------------------------------------------------------------------'
Write-Output "keystore.p12 wird in $keystoreDir erstellt..."
keytool -genkeypair -v -keystore $keystoreDir\keystore.p12 `
    -alias $alias -storepass $password -keypass $password -validity $duration `
    -keyalg RSA -keysize $keySize -sigalg SHA512withRSA `
    -dname "EMAILADDRESS=$email,CN=$name,OU=Softwarearchitektur\, Software Engineering\, Microservices\, Frameworks für Python,O=Hochschule Karlsruhe,L=Karlsruhe,S=Baden Wuerttemberg,C=de" `
    -ext SAN="$rechnernamen,DNS:localhost,IP:127.0.0.1,IP:10.0.2.2,IP:$ip"

Write-Output ''
Write-Output '--------------------------------------------------------------------------------'
Write-Output 'certificate.cer im Format "DER" wird exportiert...'
keytool -v -exportcert -file $certDir\certificate.cer `
    -keystore $keystoreDir\keystore.p12 `
    -alias $alias -storepass $password

Write-Output ''
Write-Output '--------------------------------------------------------------------------------'
Write-Output "truststore.p12 wird in $keystoreDir aus dem Zertifikat erstellt..."
keytool -importcert -v -noprompt -keystore $keystoreDir\truststore.p12 `
    -file $certDir\certificate.cer `
    -alias $alias -storepass $password

Write-Output ''
Write-Output '--------------------------------------------------------------------------------'
Write-Output 'truststore.p12:'
keytool -v -list -keystore $keystoreDir\truststore.p12 -storepass $password

# Android API-Level < 24
#Write-Output ''
#Write-Output 'truststore.bks wird erstellt...'
#keytool -importkeystore -srckeystore $keystoreDir\truststore.p12 -srcstoretype PKCS12 -srcstorepass $password `
#    -destkeystore $keystoreDir\truststore.bks -deststoretype BKS -deststorepass $password `
#    -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath C:\Software\bouncycastle\bcprov-jdk15on.jar

# https://repo.maven.apache.org/maven2/org/bouncycastle/bcprov-jdk18on
# https://repo.maven.apache.org/maven2/org/bouncycastle/bcprov-jdk15on
# https://www.bouncycastle.org/latest_releases.html
#Write-Output ''
#Write-Output 'truststore.bks:'
#keytool -v -list -keystore truststore.bks -storepass zimmermann -storetype BKS `
#    -provider org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath C:\Software\bouncycastle\bcprov-jdk18on.jar
