rm -r bin/*
cd src
echo "compiling source code.."
javac -cp .:../lib/json.jar -d ../bin --module-path $1 --add-modules javafx.controls,javafx.media,javafx.web com/orangomango/projectile/*.java -Xlint:deprecation
cd ../bin
echo "executing binary files.."
java -cp .:../lib/json.jar --module-path $1 --add-modules javafx.controls,javafx.media,javafx.web com.orangomango.projectile.Launcher
