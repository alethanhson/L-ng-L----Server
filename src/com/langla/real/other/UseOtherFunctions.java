package com.langla.real.other;

import com.langla.data.SkillClan;
import com.langla.lib.Utlis;
import com.langla.real.item.ItemHandle;
import com.langla.real.player.Char;
import com.langla.server.lib.Message;
import com.langla.utlis.UTPKoolVN;

/**
 * @author PKoolVN
 **/
public class UseOtherFunctions {
    public static void moRongViThu(Char character, Message message) {
        try {
            byte id = message.readByte();
            int vang = 2000;
            if(character.infoChar.vang < vang){
                character.client.session.serivce.ShowMessGold("Không đủ vàng");
                return;
            }
            if(character.infoChar.MaxNangCapViThu >= 9){
                character.client.session.serivce.ShowMessGold("Cấp đã đạt tối đa");
                return;
            }
            character.infoChar.MaxNangCapViThu++;
            character.mineVang(vang, true, true, "Mở rộng vĩ thú");
            character.client.session.serivce.ShowMessGold("Đã mở rộng tất cả kĩ năng thêm 1 cấp");
        } catch (Exception ex) {
            Utlis.logError(UseOtherFunctions.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public static void nangCapViThu(Char character, Message message) {
        try {
            byte id = message.readByte();
            boolean isVang = message.readBoolean();

            SkillClan skillViThu = character.listSkillViThu.get(id);
            if(skillViThu == null ) return;

            if(skillViThu.levelNeed >= character.infoChar.MaxNangCapViThu){
                character.client.session.serivce.ShowMessGold("Kỹ năng đã đạt cấp tối đa hay mở rộng thêm để nâng cấp");
                return;
            }

            if(isVang || skillViThu.id == 19){
                int vang = 1000;
                if(skillViThu.id == 19) vang = 1500;
                if(character.infoChar.vang < vang){
                    character.client.session.serivce.ShowMessGold("Không đủ vàng");
                    return;
                }
                character.mineVang(vang, true, true, "Nâng cấp vĩ thú");
            } else {
                if(character.infoChar.bacKhoa < 50000000){
                    character.client.session.serivce.ShowMessGold("Không đủ bạc khóa");
                    return;
                }
                character.mineBacKhoa(50000000, true, true, "Nâng cấp vĩ thú");
            }
            int lv = skillViThu.levelNeed + 1;

            skillViThu.setLevelViThu(lv);

            character.msgUpdateSkillViThu();
            character.setUpInfo(true);
        } catch (Exception ex) {
            Utlis.logError(UseOtherFunctions.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }

    }

    public static void xoaViThu(Char character, Message message) {
        try {
            byte id = message.readByte();
            boolean isVang = message.readBoolean();

            SkillClan skillViThu = character.listSkillViThu.get(id);
            if(skillViThu == null ) return;
            if(skillViThu.id == 19){
                character.client.session.serivce.ShowMessGold("Không thể xóa kỹ năng đặc biệt");
                return;
            }
            if(isVang){
                if(character.infoChar.vang < 500){
                    character.client.session.serivce.ShowMessGold("Không đủ vàng");
                    return;
                }
                character.mineVang(500, true, true, "Xóa vĩ thú");
            } else {
                if(character.infoChar.bacKhoa < 30000000){
                    character.client.session.serivce.ShowMessGold("Không đủ bạc khóa");
                    return;
                }
                character.mineBacKhoa(30000000, true, true, "Xóa vĩ thú");
            }

            character.listSkillViThu.remove(skillViThu);
            character.msgUpdateSkillViThu();
            character.setUpInfo(true);

        } catch (Exception ex) {
            Utlis.logError(UseOtherFunctions.class, ex , "Da say ra loi:\n" + ex.getMessage());
        }

    }
}
