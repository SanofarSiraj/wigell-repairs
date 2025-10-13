@echo off
echo Stopping wigell-repairs
docker stop wigell-repairs-container
echo Deleting container wigell-repairs
docker rm wigell-repairs-container
echo Deleting image wigell-repairs
docker rmi wigell-repairs
echo Running mvn package
call mvn package -DskipTests
echo Creating image wigell-repairs
docker build -t wigell-repairs:latest .
echo Creating and running container wigell-repairs
docker run -d -p 5555:5555 --name wigell-repairs-container --network services-network wigell-repairs:latest
echo Done!