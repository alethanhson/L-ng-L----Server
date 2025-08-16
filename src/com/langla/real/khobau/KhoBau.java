package com.langla.real.khobau;

import com.langla.data.DataCenter;
import com.langla.lib.Utlis;
import com.langla.real.item.Item;
import com.langla.real.player.Char;

import java.util.Arrays;
import java.util.List;

/**
 * @author PKoolVN
 **/
public class KhoBau {
    protected static KhoBau Instance;

    public static KhoBau gI() {
        if (Instance == null)
            Instance = new KhoBau();
        return Instance;
    }

    // vp thưởng , 1 hỏa lực, 2 bạc, 3 vàng, 4 đá
    public List<Integer> idHoaLuc = Arrays.asList(0, 9, 18, 4, 17);

    public List<Integer> idBac = Arrays.asList(2, 5, 19, 10, 1);

    public List<Integer> idVangKhoa = Arrays.asList(8, 13, 16, 21, 12);

    public List<Integer> idDa = Arrays.asList(7, 11, 20, 15, 6);

    public List<Integer> idRuong = Arrays.asList(3, 14);

    public List<Integer> idHiem = Arrays.asList(3, 14, 15, 6, 21, 12, 10, 1, 4, 17);

    public void Star(Char chars) {
        chars.quayKhoBau = new KhoBauTpl();
        chars.client.session.serivce.SendKhoBau();
    }

    public void Quay(Char chars, byte muccuoc) {
        try {
            int minevoso = 2;
            if (muccuoc == 1) {
                minevoso = 10;
            } else if (muccuoc == 2) {
                minevoso = 50;
            }
            int vosobag = chars.getAmountAllById(176);
            if (vosobag < minevoso) {
                chars.client.session.serivce.ShowMessGold("Không có đủ vỏ sò");
                return;
            }
            chars.removeAmountAllItemBagById(176, minevoso, "Quay vỏ sò");

            int sao = 0;
            int item = 0;
            // nếu lần đầu quay random 1-3 sao mức cược cao thì tỉ lệ càng cao, mức cược từ
            // 1,2
            if (chars.quayKhoBau.solanquay == 0) {
                if (muccuoc == 1) {
                    if (Utlis.nextInt(100) < 2) {
                        sao = Utlis.nextInt(2);
                    }
                } else if (muccuoc == 2) {
                    if (Utlis.nextInt(100) < 5) {
                        sao = Utlis.nextInt(2);
                    }
                } else {
                    sao = 0;
                }
            } else {
                int tile = Utlis.nextInt(1, 100);
                // Tăng tile tùy vào giá trị của muccuoc
                int phanTramCong = 0;
                if (muccuoc == 1) {
                    phanTramCong = 1; // Tăng 1% cho muccuoc == 1
                } else if (muccuoc == 2) {
                    phanTramCong = 3; // Tăng 3% cho muccuoc == 2
                }
                if (tile <= 1 + phanTramCong) { // 1%
                    sao = 6;
                } else if (tile <= 2 + phanTramCong) { // 1%
                    sao = 5;
                } else if (tile <= 4 + phanTramCong) { // 2%
                    sao = 4;
                } else if (tile <= 7 + phanTramCong) { // 3%
                    sao = 3;
                } else if (tile <= 11 + phanTramCong) { // 4%
                    sao = 2;
                } else if (tile <= 16 + phanTramCong) { // 5%
                    sao = 1;
                } else { // 83%
                    sao = -2;
                }
            }
            // kiểm tra xem mức cược có đúng với lần đầu không
            if (chars.quayKhoBau.solanquay > 0 && chars.quayKhoBau.cuoc != muccuoc) {
                chars.client.session.serivce.ShowMessGold("Đã sảy ra lỗi. vui lòng thoát tab kho báu và thử lại");
                return;
            }

            if (chars.quayKhoBau.itemchay == -1) { // quay lần đầu
                item = Utlis.nextInt(0, 21, getArr(idHiem));
            } else {
                int tile = Utlis.nextInt(100);
                // Tăng tile tùy vào giá trị của muccuoc
                int phanTramCong = 0;
                if (muccuoc == 1) {
                    phanTramCong = 1; // Tăng 1% cho muccuoc == 1
                } else if (muccuoc == 2) {
                    phanTramCong = 3; // Tăng 3% cho muccuoc == 2
                }
                if (tile <= (1 + phanTramCong) && sao <= 3) { // 1 % ra tất cả
                    item = Utlis.nextInt(0, 21);
                } else if (tile <= (3 + phanTramCong) && sao <= 3) { // 2 % ra vật phẩm cao. - rương, có trùng lặp
                    item = Utlis.nextInt(0, 21, getArr(idRuong));
                } else if (tile <= (8 + phanTramCong)) { // 5 % ra vật phẩm cao. -rương, không trùng lặp
                    item = Utlis.nextInt(0, 21, getArr(idRuong, getListItem(chars.quayKhoBau.itemchay)));
                } else {// còn lại ra vật phẩm hiếm không trùng
                    item = Utlis.nextInt(0, 21, getArr(idHiem, getListItem(chars.quayKhoBau.itemchay)));
                }
            }
            chars.client.session.serivce.sendQuayKhoBau((byte) sao, (byte) item, (byte) getTypeItem(item),
                    checkRuong(item));

            // kiểm tra xem có trùng lặp vật phẩm đã quay hay không
            List<Integer> itemcu = getListItem(chars.quayKhoBau.itemchay);
            List<Integer> itemmoi = getListItem(item);
            if (itemcu != null && itemmoi != null && itemcu.size() == itemmoi.size()) {
                for (int i = 0; i < itemcu.size(); i++) {
                    if (itemcu.get(i).equals(itemmoi.get(i))) {
                        chars.quayKhoBau.xtrunglap = itemmoi.indexOf(item);
                        break;
                    }
                }
            } else {
                chars.quayKhoBau.xtrunglap = -1;
            }
            // update vào nhân vật
            chars.quayKhoBau.solanquay++;
            chars.quayKhoBau.cuoc = muccuoc;
            chars.quayKhoBau.itemchay = item;
            chars.quayKhoBau.textvp = getTypeItem(item);
            chars.quayKhoBau.ruong = checkRuong(item);
            chars.quayKhoBau.sosao = sao;

            // nếu sao -2 thì reset
            if (sao == -2) {
                chars.quayKhoBau = new KhoBauTpl();
            }

        } catch (Exception ex) {
            Utlis.logError(KhoBau.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public void NhanThuong(Char chars, short type) {
        if (chars.getCountNullItemBag() <= 0) {
            chars.client.session.serivce.ShowMessWhite("Hành trang không đủ chỗ chứa");
            return;
        }
        if (type == 177) { // nhận rương
            if (chars.quayKhoBau.ruong) {
                chars.quayKhoBau = new KhoBauTpl(); // reset
                Item ruong = new Item(177);
                chars.addItem(ruong, "Nhận thưởng vòng quay");
                chars.msgAddItemBag(ruong);
            }
        } else if (type == 1) { // vỏ sò
            if (chars.quayKhoBau.sosao >= 0) {
                int sosao = chars.quayKhoBau.sosao;
                if (chars.quayKhoBau.xtrunglap >= 0) {
                    sosao += getSaoTrungLap(chars.quayKhoBau.xtrunglap);
                }
                if (sosao > 6)
                    sosao = 6;
                int voso = DataCenter.gI().dataGiftQuaySo[4][sosao];
                chars.quayKhoBau = new KhoBauTpl(); // reset
                Item item = new Item(176, false, voso);
                chars.addItem(item, "Nhận từ vòng quay");
                chars.msgAddItemBag(item);
            }
        } else if (type == 0) { // nhận thưởng
            if (chars.quayKhoBau.sosao >= 0) {
                int sosao = chars.quayKhoBau.sosao;
                if (chars.quayKhoBau.xtrunglap >= 0) {
                    sosao += getSaoTrungLap(chars.quayKhoBau.xtrunglap);
                }
                if (sosao > 6)
                    sosao = 6;

                if (chars.quayKhoBau.textvp == 0) { // hỏa lưc
                    int sl = DataCenter.gI().dataGiftQuaySo[chars.quayKhoBau.textvp][sosao];
                    Item item = new Item(182, false, sl);
                    chars.addItem(item, "Nhận từ vòng quay");
                    chars.msgAddItemBag(item);
                } else if (chars.quayKhoBau.textvp == 1) { // bạc
                    int bac = DataCenter.gI().dataGiftQuaySo[chars.quayKhoBau.textvp][sosao];
                    chars.addBac(bac, true, true, "Nhận từ vòng quay");
                } else if (chars.quayKhoBau.textvp == 2) { // vàng khóa
                    int vang = DataCenter.gI().dataGiftQuaySo[chars.quayKhoBau.textvp][sosao];
                    chars.addVangKhoa(vang, true, true, "Nhận từ vòng quay");
                } else if (chars.quayKhoBau.textvp == 3) { // đá
                    int id = DataCenter.gI().dataGiftQuaySo[chars.quayKhoBau.textvp][sosao];
                    Item item = new Item(id);
                    chars.addItem(item, "Nhận từ vòng quay");
                    chars.msgAddItemBag(item);
                }
                chars.quayKhoBau = new KhoBauTpl(); // reset
            }
        }
        chars.client.session.serivce.ResetKhoBau();
    }

    public int getTypeItem(int id) {
        if (idHoaLuc.contains(id)) {
            return 0;
        } else if (idBac.contains(id)) {
            return 1;
        } else if (idVangKhoa.contains(id)) {
            return 2;
        } else if (idDa.contains(id)) {
            return 3;
        }
        return -1;
    }

    public int getSaoTrungLap(int index) {
        if (index <= 2) {
            return 1;
        } else if (index == 3) {
            return 2;
        } else if (index == 4) {
            return 3;
        }
        return 0;
    }

    public List<Integer> getListItem(int id) {
        if (idHoaLuc.contains(id)) {
            return idHoaLuc;
        } else if (idBac.contains(id)) {
            return idBac;
        } else if (idVangKhoa.contains(id)) {
            return idVangKhoa;
        } else if (idDa.contains(id)) {
            return idDa;
        }
        return null;
    }

    public boolean checkRuong(int id) {
        if (idRuong.contains(id)) {
            return true;
        } else {
            return false;
        }
    }

    public int[] getArr(List<Integer> list) {
        try {
            return list.stream().mapToInt(i -> i).toArray();
        } catch (Exception ex) {
            Utlis.logError(KhoBau.class, ex, "Da say ra loi:\n" + ex.getMessage());
            return null;
        }
    }

    public int[] getArr(List<Integer>... lists) {
        try {
            // Sử dụng flatMap để xử lý nhiều danh sách và tạo một stream duy nhất
            return Arrays.stream(lists)
                    .flatMap(List::stream)
                    .mapToInt(i -> i)
                    .toArray();
        } catch (Exception ex) {
            Utlis.logError(KhoBau.class, ex, "Đã xảy ra lỗi:\n" + ex.getMessage());
            return null;
        }
    }

}
