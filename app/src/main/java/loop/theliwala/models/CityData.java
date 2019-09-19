package loop.theliwala.models;

/**
 * Created by LALIT on 8/14/2017.
 */

public class CityData {

        private int CityId;
        private String CityName;

        public int getCityId ()
        {
            return CityId;
        }

        public void setCityId (int CityId)
        {
            this.CityId = CityId;
        }

        public String getCityName ()
        {
            return CityName;
        }

        public void setCityName (String CityName)
        {
            this.CityName = CityName;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [CityId = "+CityId+", CityName = "+CityName+"]";
        }

    }
