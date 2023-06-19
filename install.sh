#!/bin/bash

# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(pwd)

# kill java process
killProcess() {
    echo "Working path: " . $SCRIPTPATH;
    kill -9 $(ps aux | grep "java -cp" | grep -v 'grep' | awk '{print $2}')
    rm -rf */*/libs
}

# runVbee
runVbee() {
    echo "Start building Vbee..."
    currentDir="api/vbee"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:api/vbee/libs/*:api/vbee/build/libs/vbee-1.0.jar" com.vinplay.vbee.main.VBeeMain >/dev/null > logvbee.log 2>&1 &
	echo "Finish run Vbee..."
}
# runBackend
runBackend() {
    echo "Start building Backend..."
    currentDir="api/VinPlayBackend"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:api/VinPlayBackend/libs/*:api/VinPlayBackend/build/libs/VinPlayBackend-1.0.jar" com.vinplay.api.backend.server.VinPlayBackendMain >/dev/null > logbackend.log 2>&1 &
	echo "Finish run Backend..."
}
# runPortal
runPortal(){
    echo "Start building Portal..."
    currentDir="api/VinPlayPortal"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:api/VinPlayPortal/libs/*:api/VinPlayPortal/build/libs/VinPlayPortal.jar" com.vinplay.api.server.JettyServer >/dev/null > logportal.log 2>&1 &
	echo "Finish run Portal..."
}
# runWspay
runWspay(){
    echo "Start building Wspay..."
    currentDir="api/wspay"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:api/wspay/libs/*:api/wspay/build/libs/wspay.jar" com.vinplay.pay.server.JettyServer >/dev/null > logwspay.log 2>&1 &
	echo "Finish run Wspay..."
}
# runMiniGame
runMiniGame(){
    currentDir="game/Minigame"
    cd $currentDir
    ../../gradlew clean
    ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:game/Minigame/libs/*:game/Minigame/build/libs/Minigame.jar" game.MiniGameMain >/dev/null 2>&1 &

}
# runSlot
runSlot(){
    echo "Start building Slot..."
    currentDir="game/slot"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:${currentDir}/libs/*:${currentDir}/build/libs/SlotMachine.jar" game.SlotMain >/dev/null > logslot.log 2>&1 &
	echo "Finish run Slot..."
}
# runBacay
runBacay(){
    echo "Start building Bacay..."
    currentDir="game/bacayServer"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:${currentDir}/libs/*:${currentDir}/build/libs/bacayServer.jar" game.bacay.server.BacayMain >/dev/null > logbacay.log 2>&1 &
	echo "Finish run Bacay..."
}
# runTienLen
runTienLen(){
    echo "Start building TienLen..."
    currentDir="game/tlmn"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:${currentDir}/libs/*:${currentDir}/build/libs/tlmn.jar" game.tienlen.server.TlmnMain >/dev/null > logtlmn.log 2>&1 &
	echo "Finish run TienLen..."
}
#runGameAccess
#runGameAccess(){
#    echo "Start building GameAccess..."
#    currentDir="api/GameAccess"
#    cd $currentDir
#     ../../gradlew clean
#     ../../gradlew build
#    cd $SCRIPTPATH;
#    nohup java -cp "libs/*:api/GameAccess/libs/*:api/GameAccess/build/libs/GameAccess.jar" com.vinplay.server.JettyServer >/dev/null 2>&1 &
#	echo "Finish run GameAccess..."
#}
# runXocDia
runXocDia(){
    echo "Start building Xoc Dia..."
    currentDir="game/xocdia"
    cd $currentDir
     ../../gradlew clean
     ../../gradlew build
    cd $SCRIPTPATH;
    nohup java -cp "libs/*:${currentDir}/libs/*:${currentDir}/build/libs/xocdia.jar" game.xocdia.server.XocDiaMain >/dev/null > logxocdia.log 2>&1 &
    echo "Finish run Xoc Dia..."
}



runAllApi(){
  runVbee
  runPortal
  runWspay
  runBackend
  #runGameAccess
}
main(){
  killProcess
  sleep 5
  runAllApi
  runMiniGame
  runSlot
  runTienLen
  runBacay
  runXocDia
}
main