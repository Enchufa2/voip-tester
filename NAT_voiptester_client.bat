@echo off

if [%1] == [] (
	echo.
	echo Usage: %0 ^<public_IP_address^>
	echo.
	echo ^<public_IP_address^>: public IP address of the router performing NAT
	echo.
	echo Example: %0 150.214.27.1
	echo.
	exit /B /0
)

set PUBLICIPADDRESS=%1

call path.bat
java -Djava.rmi.server.hostname=%PUBLICIPADDRESS% -jar voiptester_client.jar

echo on
