package Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherData {

    private String currentDayTimeStamp;
    private String minWaveHeight;
    private String maxWaveHeight;
    private String wind;
    private String temp;
    private int dayString;
    private String[] days = new String[]{
            "Sunady", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    public WeatherData() {


    }

    public void parseJSON(JSONArray response, int index) {

        try {
            // Get date and time
            JSONObject currentDayBaseIndexJO = response.getJSONObject(index);
            currentDayTimeStamp = currentDayBaseIndexJO
                    .getString("localTimestamp");


            long timestampLong = Long.parseLong(currentDayTimeStamp) * 1000;
            String date = new SimpleDateFormat("dd/MM/yyyy")
                    .format(new Date(timestampLong));


            Date timeOfDay = new Date(timestampLong);
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeOfDay);
            int AM_PM_hour = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            dayString = cal.get(Calendar.DAY_OF_WEEK);


            currentDayTimeStamp = date + " " + AM_PM_hour + ":00";


            // Get minimum wave height
            JSONObject swellJO = currentDayBaseIndexJO.getJSONObject("swell");
            minWaveHeight = swellJO.getString("minBreakingHeight");

            // Get maximum wave height
            maxWaveHeight = swellJO.getString("maxBreakingHeight");

            // Get wind speed
            JSONObject windJO = currentDayBaseIndexJO.getJSONObject("wind");
            wind = windJO.getString("speed");

            // Get temperature
            JSONObject tempJO = currentDayBaseIndexJO
                    .getJSONObject("condition");
            temp = tempJO.getString("temperature");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getCurrentDayTimeStamp() {
        return currentDayTimeStamp;
    }

    public String getWindSpeed() {
        return wind + "Kph";
    }

    public String getWavesHeight() {
        return minWaveHeight + "m" + " - " + maxWaveHeight + "m";
    }

    public String getDayString() {
        return days[dayString - 1];
    }

    public String getMinimumWavesHeight() {
        return minWaveHeight + "m";
    }

    public String getRawMinimumWavesHeight() {

        return minWaveHeight;
    }

}
