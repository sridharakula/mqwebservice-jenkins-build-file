import java.util.Vector;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class PropReader {

    public static void main(String[] args) {
        try {

            System.out.println("********");
            InputStream input = new FileInputStream("data.properties");
            Properties prop = new Properties();
            prop.load(input);
            String names = prop.getProperty("names");
            Vector<String> nameList = new Vector<String>();
            populateData(names, nameList);
            String ccodes = prop.getProperty("cCodes");
            Vector<String> ccodeList = new Vector<String>();
            populateData(ccodes, ccodeList);
            String serviceCodes = prop.getProperty("serviceCodes");
            Vector<String> serviceCodeList = new Vector<String>();
            populateData(serviceCodes, serviceCodeList);
            String checkWarningMessage=prop.getProperty("checkWarningMessage");
            String restMessage=prop.getProperty("restMessage");
            String shipmentMsg=prop.getProperty("shipmentMsg");
            String delim=prop.getProperty("delim");
            String delim1=prop.getProperty("delim1");
            boolean bName=Boolean.getBoolean(prop.getProperty("bName"));
            boolean bExecDate=Boolean.getBoolean(prop.getProperty("bExecDate"));
            boolean bCcode=Boolean.getBoolean(prop.getProperty("bCcode"));
            boolean bScode=Boolean.getBoolean(prop.getProperty("bScode"));
            System.out.println("********");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private static void populateData(String values, Vector<String> list) {
        String[] tokens = values.split(",");
        for(String value:tokens) {
            list.add(value);
        }
    }

}
