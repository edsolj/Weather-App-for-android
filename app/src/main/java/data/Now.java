package data;

import com.google.gson.annotations.SerializedName;

public class Now {
    public String cloud;
    public String cond_code;
    public String cond_txt;

    @Override
    public String toString() {
        return "Now{" +
                "cloud='" + cloud + '\'' +
                ", cond_code='" + cond_code + '\'' +
                ", cond_txt='" + cond_txt + '\'' +
                ", feelingTemp='" + feelingTemp + '\'' +
                ", humidity='" + humidity + '\'' +
                ", precipitation='" + precipitation + '\'' +
                ", pres='" + pres + '\'' +
                ", tmp='" + tmp + '\'' +
                ", visibility='" + visibility + '\'' +
                ", wind_deg='" + wind_deg + '\'' +
                ", wind_dir='" + wind_dir + '\'' +
                ", windForce='" + windForce + '\'' +
                ", wind_spd='" + wind_spd + '\'' +
                '}';
    }

    @SerializedName("fl")
    public String feelingTemp;
    @SerializedName("hum")
    public String humidity;
    @SerializedName("pcpn")
    public String precipitation;
    public String pres;
    public String tmp;
    @SerializedName("vis")
    public String visibility;
    public String wind_deg;
    public String wind_dir;
    @SerializedName("wind_sc")
    public String windForce;
    public String wind_spd;
}
