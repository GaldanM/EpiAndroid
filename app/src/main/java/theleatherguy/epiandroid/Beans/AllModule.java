package theleatherguy.epiandroid.Beans;

import java.util.List;

/**
 * Created by olivier.medec on 30/01/2016.
 */
public class AllModule {
    //public String[] preload = null;
    public class Item {
                public String id = null;
                public String title_cn = null;
                public String semester = null;
                public String num = null;
                public String begin = null;
                public String end = null;
                public String end_register = null;
                public String scolaryear = null;
                public String code = null;
                public String codeinstance = null;
                public String location_title = null;
                public String instance_location = null;
                public String flags = null;
                public String credits = null;
                public String status = null;
                public String waiting_grades = null;
                public int active_promo = 0;
                public int open = 0;
                public String title = null;
    }
    public List<Item> items = null;
}
