package theleatherguy.epiandroid.Beans;

import java.util.List;

import theleatherguy.epiandroid.Adapters.ListPlanning;

/**
 * Created by olivier.medec on 31/01/2016.
 */
public class Projet {
    public class note {
        public String scolaryear = null;
        public String codemodule = null;
        public String titlemodule = null;
        public String codeinstance = null;
        public String codeacti = null;
        public String title = null;
        public String date = null;
        public String correcteur = null;
        public String final_note = null;
        public String comment = null;
    }

    public List<note> notes;
}
