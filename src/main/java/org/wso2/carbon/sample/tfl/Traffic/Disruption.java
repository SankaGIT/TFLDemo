package org.wso2.carbon.sample.tfl.Traffic;

import com.vividsolutions.jts.algorithm.ConvexHull;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.geometry.jts.JTSFactoryFinder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanka on 2/6/15.
 */
public class Disruption {
    String id;
    String state;
    String severity;
    String location;
    String comments;
    String coordinates = null;

    public boolean isMultiPolygon = true;
    final static double tolerance = 0.0005;
    ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

    public Disruption() {

    }

    public Disruption(String id, String severity, String location, String comments) {
        this.id = id;
        this.state = severity;
        this.comments = comments;
        this.location = location;
    }

    public void setCoordsPoly(String coords) {
        isMultiPolygon = false;
        String[] temp = coords.split(",");
        StringBuilder sb = new StringBuilder("{ \n 'type': 'Polygon', \n 'coordinates': [[");
        for (int i = 0; i < temp.length - 1; i += 2) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("[").append(Double.parseDouble(temp[i])).append(",").append(Double.parseDouble(temp[i + 1])).append("]");
        }
        sb.append("]] \n }");
        coordinates = sb.toString();
    }

    public void setCoordsPoly(Coordinate[] coords) {
        StringBuilder sb = new StringBuilder("{ \n 'type': 'Polygon', \n 'coordinates': [[");
        for (int i = 0; i < coords.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("[").append(coords[i].x).append(",").append(coords[i].y).append("]");
        }
        sb.append("]] \n }");
        coordinates = sb.toString();
    }

    public void addCoordsLane(String co) {
        String[] temp = co.split(",");
        if (temp.length != 4) {
            System.out.println(co);
            return;
        }
        try {
            double x1, x2, y1, y2;
            x1 = Double.parseDouble(temp[0]);
            y1 = Double.parseDouble(temp[1]);
            x2 = Double.parseDouble(temp[2]);
            y2 = Double.parseDouble(temp[3]);

            double f = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow(y1 - y2, 2));
            coords.add(new Coordinate(Double.parseDouble(temp[0]) - tolerance * (y2 - y1) / f, Double.parseDouble(temp[1]) - tolerance * (x1 - x2) / f));
            coords.add(new Coordinate(Double.parseDouble(temp[0]) + tolerance * (y2 - y1) / f, Double.parseDouble(temp[1]) + tolerance * (x1 - x2) / f));
            coords.add(new Coordinate(Double.parseDouble(temp[2]) + tolerance * (y2 - y1) / f, Double.parseDouble(temp[3]) + tolerance * (x1 - x2) / f));
            coords.add(new Coordinate(Double.parseDouble(temp[2]) - tolerance * (y2 - y1) / f, Double.parseDouble(temp[3]) - tolerance * (x1 - x2) / f));

            //coords.add(new Coordinate(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])));
            //coords.add(new Coordinate(Double.parseDouble(temp[2]), Double.parseDouble(temp[3])));
        } catch (NumberFormatException e) {
            System.out.println("NFE " + co);
        }
    }

    /*
        public void addCoordsLane(String coords) {

        }
    */
    public void end() {
        if (isMultiPolygon) {

            Coordinate[] c = new Coordinate[coords.size()];
            c = coords.toArray(c);
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            ConvexHull ch = new ConvexHull(c, geometryFactory);
            //System.out.println(ch.getConvexHull().toString());
            setCoordsPoly(ch.getConvexHull().getCoordinates());
        }

    }

    @Override
    public String toString() {
        return "{'id': "+id+", \n"
                +"'properties': { \n"
                +" 'timeStamp': "+System.currentTimeMillis()+", \n"
                +" 'state': '"+severity+"', \n"
                +" 'information': "+"'Location- "+location+" Comments- "+comments+"'"+"\n"
                +" }, \n"
                +"'geometry' : "+coordinates+"\n}";
    }


    public void setComments(String comments) {
        this.comments = comments.replaceAll("'", "").replaceAll("\"","");
    }

    public void setLocation(String location) {
        this.location = location.replaceAll("'", "").replaceAll("\"","");
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getState() {
        return state;
    }

}
