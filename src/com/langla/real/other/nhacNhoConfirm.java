package com.langla.real.other;

import com.langla.lib.Utlis;
import com.langla.real.player.Client;

/**
 * @author PKoolVN
 **/
public class nhacNhoConfirm {
    public static void Confirm(Client client){
        try {
            if(client.TypeConfirm == -1) return;
            switch (client.TypeConfirm){
                case 0:

                    break;

                case 1:

                    break;
            }
        }  catch (Exception ex) {
        Utlis.logError(nhacNhoConfirm.class, ex , "Da say ra loi:\n" + ex.getMessage());
    }

    }
}
