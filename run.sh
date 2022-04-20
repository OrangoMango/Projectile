cd src
echo "compiling source code.."
javac -d ../bin --module-path $FX_PATH --add-modules javafx.controls,javafx.media com/orangomango/projectile/*.java
cd ../bin
echo "executing binary files.."
java --module-path $FX_PATH --add-modules javafx.controls,javafx.media com.orangomango.projectile.MainApplication
