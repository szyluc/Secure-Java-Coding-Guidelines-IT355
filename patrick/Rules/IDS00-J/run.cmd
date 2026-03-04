@echo off
javac -cp ".;../../../project/lib/sqlite-jdbc-3.51.2.0.jar" .\*.java
java --enable-native-access=ALL-UNNAMED -cp ".;../../../project/lib/sqlite-jdbc-3.51.2.0.jar" Login