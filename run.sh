cd src
echo "compiling source code.."
javac -cp .:../lib/json.jar -d ../bin --module-path $FX_PATH --add-modules javafx.controls,javafx.media com/orangomango/projectile/*.java -Xlint:deprecation
cd ../bin
echo "executing binary files.."
java -cp .:../lib/json.jar --module-path $FX_PATH --add-modules javafx.controls,javafx.media com.orangomango.projectile.MainApplication