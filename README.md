# Software Engineering
Das Git Projekt enthält alle nötigen grundlegenden Dateien, um die Aufgaben zu lösen.

## Requirements
- Java 17
- IntelliJ

## Setup Lokaler Rechner
- neueste IntelliJ Version herunterladen
- Mittels git clone \<repolink-hier-einfügen> lokal herunterladen \(geht auch über IntelliJ\).
- Projekt in IntelliJ öffnen
- Dependencies herunterladen (geschieht automatisch, kann 2 min dauern).
- <img src="/doc-images/build.png" width="400px">
- in das VPN einwählen
- Maven clean und package ausführen (Maven Menü in der Leiste rechts)
- <img src="/doc-images/maven.png" width="400px">
- src/main/java/de/ostfalia/application/Application.java starten <b>oder</b> auf den Play Button oben rechts drücken
- ggf schlägt IntelliJ nach Starten vor weitere Dependencys mit "npm install" heruterzuladen \(mit Button bestätigen\). Sie sollte nach Installation der npm Packete den node_modules Ordner im Projekt sehen.</br>
- <img src="/doc-images/build.png" width="400px">
- Server mit der Anwendung läuft unter dem Tab Services \(siehe Leiste unten\)

### Falls das Builden des Frontends nicht funktioniert 
- maven clean und package im Production Modus durchführen. Dafür den Haken bei production setzen in dem Abschnitt Profiles.
  -  Im Production Modus werden die Frontend Bundles erstellt. Wenn man sich den Log von package genauer ansieht
  steht dort "Running Vite...". Zu dem Zeitpunkt werden die Frontend Bundles erstellt.
- Erneut den Server über den Button starten. Der initiale Prozess, dass die Bundles erstellt werden, wurde dann durchgeführt.
In der Regel passiert dies durch das Aufrufen der Webseite im Browser. Sie können die Bundles unter src/main/bundles im Git tracken, müssen Sie aber nicht.
  Danach kann die Anwenung wieder ohne Production Modus laufen. Dafür Haken bei Production entfernen,
  maven clean package durchführen und erneut starten. Anwendung läuft dann im Development Modus und gibt auf der Konsole mehr Logs aus.

## Setup auf dem Raspberry PI
### Erstes Aufsetzen
- ins VPN einwählen
- ssh seuser@ipadresse
- einloggen mit passwort "youshouldchangeme"
- Maven installieren mit: 
  - sudo apt install maven
- sudo apt-get install curl gnupg2 -y
- Node Version Manager für npm installieren: 
  - curl https://raw.githubusercontent.com/nvm-sh/nvm/master/install.sh  | bash
- source ~/.bashrc
- Node Version 20.17 herunterladen
  - nvm install v20.17.0
- Node Version auf 20.17 stellen. npm sollte jetzt min. version 10 haben
  - nvm use v20.17.0
- git clone \<pfad\>
- cd repositoryName
- mvn clean package -Pproduction
- Java Jar starten
  - java -jar target/softwareengineering-1.0-SNAPSHOT.jar
- Anwendung läuft auf Port 8081
  - falls nicht in der src/main/ressources/application.properties schauen
  - der Port ist mit server.port=${PORT:8081} angegeben
Das erste Starten der Anwendung kann durch einen längeren Build Process etwas dauern.

### Neue Änderungen herunterladen und starten
- mit ssh auf den PI einwählen (siehe oben)
- git pull origin main 
- prozess mit dem port 8081 beenden
- erneut die jar starten

## Projekt Struktur
- application/views: Enthält alle Frontend Oberflächen
- application/data: Alle Logik Klassen liegen in diesem Verzeichnis
- application/data/entity: Klassen, die einzelne Objekte der Influx Datenbank abbilden
- application/data/queryBuilder: enthält einen sehr simplen Builder für das herauslesen von Messwerten
- application/data/records: enthält Klassen, die einen einzelnen Messwert für einen Bucket der Influx DB mappen
- application/data/service: hier liegen die Services, die sich um die Logik bei der Datenverwaltung kümmern

## Nützliche Links
- Vaadin Dokumentation: https://vaadin.com/docs/latest/overview
- Java Client für Influx DB: https://github.com/influxdata/influxdb-client-java
- SO Charts Plug In:https://vaadin.com/directory/component/so-charts
- SO Charts Beispiele: https://storedobject.com/examples/?login=AUTO

## FAQ
### Der Run Button ist in IntelliJ ausgegraut und ich kann die Anwendung nicht starten.
Das Projekt neu builden (obere Leiste, Hammer Button oder Strg+9), 1-2 Min warten und dann den Run Button drücken.
### Ich bekomme eine CannotCreateTransactionException auf der Konsole. Woran liegt das?
Die Verbindung zum VPN wurde nicht hergestellt oder ist abgebrochen. An dieser Stelle entweder mit dem VPN
verbinden oder bei bestehender Verbindung die Verbindung trennen und erneut verbinden.

### Bei IntelliJ werden mir Dateien in Gelb angezeigt.
Diese Dateien liegen in der .gitignore und werden nicht versioniert.