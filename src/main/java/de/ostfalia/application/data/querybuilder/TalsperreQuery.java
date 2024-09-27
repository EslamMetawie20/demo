package de.ostfalia.application.data.querybuilder;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import de.ostfalia.application.data.records.TalsperreRecord;
import de.ostfalia.application.influx.InfluxDBConnector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TalsperreQuery {
    public static final InfluxDBClient influxDBClient=new InfluxDBConnector().buildConnection("https://influx.ise-i.ostfalia.de:8086/",
            "hokhSwPSGnwmQFXRh0pxQ3RoiPQllgGKHI2UUGVsFFtIRzNTB5by32qZxc-LnR64DA-j6GwR2CAJ4ZNlJXI9Eg==",
            "Harzerwasserwerke", "Software Engineering"
    );

    private String reservoirName;
    private String start;
    private String end;
    HashSet<String> fields = new HashSet<>();

    /**
     * Name der Talsperre angeben
     * @param name wie Eckertalsperre, Odertalsperre usw
     * @return this
     */
    public TalsperreQuery reservoirName(String name){
        this.reservoirName = name;
        return this;
    }

    /**
     * Start kann Werte wie 30d, 7d 24h oder 15m enthalten
     * @param start als string
     * @return this
     */
    public TalsperreQuery start(String start){
        this.start=start;
        return this;
    }
    /**
     * Start kann Werte wie 30d, 24h, 15m oder now() enthalten
     * @param end als string
     * @return this
     */
    public TalsperreQuery end(String end){
        this.end=end;
        return this;
    }

    /**
     * Zuflusswerte ausgeben
     * @return this
     */
    public TalsperreQuery inflow(){
        fields.add("inflow");
        return this;
    }

    /**
     * Abgabewerte ausgeben
     * @return this
     */
    public TalsperreQuery outflow(){
        fields.add("outflow");
        return this;
    }

    /**
     * Speicherwerte ausgeben
     * @return this
     */
    public TalsperreQuery storage(){
        fields.add("storage");
        return this;
    }
    /**
     * erstellt eine Query an die Influx DB
     * Ausgabe des Outputs als Liste
     * Liste enthält die einzelnen Messwerte
     */
    public List<TalsperreRecord> create(){
        StringBuilder sb = new StringBuilder();
        sb.append("from(bucket: \"Harzerwasserwerke\")\n");
        if(start != null ){
            sb.append("|> range(start: " + start);
            if(end != null){
                sb.append(", stop:" + end);
            }
            sb.append(")\n");

        }

        sb.append("|> filter(fn: (r) => r[\"_measurement\"] == \"reservoir_data\")\n");

        if(fields.size()>0){
            sb.append("|> filter(fn: (r) =>");
            boolean appended = false;
            for (String fieldName: fields) {
                if(appended){
                    sb.append(" or ");
                }
                sb.append("r[\"_field\"] == \"" +fieldName +"\"");
                appended = true;
            }
            sb.append(")\n");
        }
        sb.append("|> filter(fn: (r) => r[\"reservoir\"] == \"" + reservoirName + "\")\n");
        sb.append("|> yield(name: \"mean\")");
        QueryApi queryApi = influxDBClient.getQueryApi();
        return queryApi.query(sb.toString(), TalsperreRecord.class);
    }
}
