package com.langla.real.other;

import com.langla.data.ItemOption;
import com.langla.lib.Utlis;
import com.langla.real.baucua.BauCua;
import com.langla.real.baucua.BauCuaTpl;
import com.langla.real.item.Item;
import com.langla.real.player.CharDB;
import com.langla.real.player.Client;
import com.langla.real.player.Player;
import com.langla.server.lib.Message;
import com.langla.server.lib.Writer;
import com.langla.utlis.UTPKoolVN;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PKoolVN
 **/
public class InputClient {
    public static void Handler(Message msg, Client client) {
        try {
            String input = msg.readUTF();
            short type = msg.readShort();
            msg.readInt();
            switch (type) {
                case 0:// quy đổi tiền
                    Pattern pattern = Pattern.compile("^\\d+$"); // ^ và $ đảm bảo toàn bộ chuỗi phải là số
                    Matcher matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        try {
                            int number = Integer.parseInt(input);
                            if (number < 1000) {
                                client.session.serivce.ShowMessGold("Số tiền tối thiều là 1.000đ");
                            } else if (number > 10000000) {
                                client.session.serivce.ShowMessGold("Số tiền tối đa là 10tr vnđ");
                            } else {
                                int vnd = Player.getVNDByPlayerId(client.player);
                                if (vnd < number) {
                                    client.session.serivce.ShowMessGold("Số dư không đủ vui lòng nạp thêm");
                                } else {
                                    if (Player.mineVND(client.player, number)) {
                                        int vang = (int) convertVNDToGold(number);
                                        client.mChar.addVang(vang, true, true, "Quy đổi từ VND");
                                        client.mChar.phucLoi.vangNapTichLuy += vang;
                                        client.mChar.phucLoi.vangNapHomNay += vang;
                                        client.mChar.phucLoi.vangNapTuan += vang;
                                        client.mChar.phucLoi.vangNapDon += vang;
                                        client.mChar.phucLoi.vangNapMoc += vang;
                                        client.mChar.infoChar.tongVangNap += vang;
                                        int diemTichQuay = vang / 100;
                                        client.mChar.phucLoi.diemTichLuyVongQuay += diemTichQuay;
                                        LocalDate currentDate = LocalDate.now();
                                        if (client.mChar.phucLoi.lastNapLienTucUpdate == null
                                                || !client.mChar.phucLoi.lastNapLienTucUpdate.isEqual(currentDate)) {
                                            // Nếu ngày cuối cùng cập nhật không phải hôm nay, cập nhật biến và ghi lại
                                            // ngày
                                            client.mChar.phucLoi.soNgayNapLienTuc++;
                                            client.mChar.phucLoi.lastNapLienTucUpdate = currentDate;
                                        }

                                        // quà nạp đầu

                                        if (!client.mChar.infoChar.isNapDau) {
                                            client.mChar.infoChar.isNapDau = true;

                                            Item item = new Item(277, true, 20);

                                            Thu thu = new Thu();
                                            thu.id = client.mChar.baseIdThu++;
                                            thu.chuDe = "Quà nạp lần đầu";
                                            thu.nguoiGui = "Hệ thống";
                                            thu.noiDung = "Phần thưởng nạp lần đầu";
                                            thu.item = item;
                                            client.mChar.listThu.add(thu);

                                            item = new Item(784, true);
                                            item.addItemOption(new ItemOption(143, 50));
                                            item.addItemOption(new ItemOption(209, 25));
                                            thu = new Thu();
                                            thu.id = client.mChar.baseIdThu++;
                                            thu.chuDe = "Quà nạp lần đầu";
                                            thu.nguoiGui = "Hệ thống";
                                            thu.noiDung = "Phần thưởng nạp lần đầu";
                                            thu.item = item;
                                            client.mChar.listThu.add(thu);

                                            item = new Item(416, true);
                                            thu = new Thu();
                                            thu.id = client.mChar.baseIdThu++;
                                            thu.chuDe = "Quà nạp lần đầu";
                                            thu.nguoiGui = "Hệ thống";
                                            thu.noiDung = "Phần thưởng nạp lần đầu";
                                            thu.item = item;
                                            client.mChar.listThu.add(thu);

                                            item = new Item(445, true);
                                            thu = new Thu();
                                            thu.id = client.mChar.baseIdThu++;
                                            thu.chuDe = "Quà nạp lần đầu";
                                            thu.nguoiGui = "Hệ thống";
                                            thu.noiDung = "Phần thưởng nạp lần đầu";
                                            thu.item = item;
                                            client.mChar.listThu.add(thu);

                                            client.session.serivce.updateThu();
                                        }
                                    } else {
                                        client.session.serivce.ShowMessGold("Có lỗi sảy ra vui lòng thử lại sau");
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            // Trường hợp chuỗi quá lớn để chuyển thành int
                            client.session.serivce.ShowMessGold("Số tiền không hợp lệ");
                        }
                    } else {
                        client.session.serivce.ShowMessGold("Số tiền không hợp lệ");
                    }
                    break;

                case 1: // đổi mật khẩu
                    pattern = Pattern.compile("^[a-zA-Z0-9]+$"); // Chỉ cho nhập số và chữ
                    matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        String lowerCaseInput = input.toLowerCase();
                        if (Player.verifyPassword(client.player, lowerCaseInput)) {
                            client.session.serivce.InputClient("Nhập mật khẩu mới", (short) 2);
                            client.isChangePass = true;
                        } else {
                            client.session.serivce.ShowMessGold("Mật khẩu hiện tại không đúng");
                        }
                    } else {
                        client.session.serivce.ShowMessGold("Mật khẩu không hợp lệ");
                    }

                    break;
                case 2: // đổi mật khẩu
                    if (!client.isChangePass)
                        break;
                    pattern = Pattern.compile("^[a-zA-Z0-9]+$"); // Chỉ cho nhập số và chữ
                    matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        String lowerCaseInput = input.toLowerCase();
                        if (Player.updatePassword(client.player, lowerCaseInput)) {
                            client.session.serivce.ShowMessGold("Đổi mật khẩu thành công");
                            client.session.serivce.closeTab();
                        }
                    } else {
                        client.session.serivce.ShowMessGold("Mật khẩu không hợp lệ");
                    }
                    client.isChangePass = false;
                    break;
                case 3:
                    pattern = Pattern.compile("^[a-zA-Z0-9]+$"); // Chỉ cho nhập số và chữ
                    matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        client.session.serivce.listGiaToc(client, input);
                    } else {
                        client.session.serivce.ShowMessGold("Tên gia tộc không hợp lệ");
                    }

                    break;

                case 4:
                    if (!BauCua.gI().isSetNumber) {
                        client.session.serivce.ShowMessGold("Đã hết thời gian đặt cược");
                        return;
                    }
                    if (BauCua.gI().checkPlayer(client.mChar.id) != null) {
                        client.session.serivce.ShowMessGold("Mỗi vòng chỉ được cược 1 lần");
                        return;
                    }
                    pattern = Pattern.compile("^\\d+$"); // ^ và $ đảm bảo toàn bộ chuỗi phải là số
                    matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        try {
                            int number = Integer.parseInt(input);
                            if (number < 10) {
                                client.session.serivce.ShowMessGold("Số tiền tối thiều là 10 vàng");
                            } else if (number > 100000) {
                                client.session.serivce.ShowMessGold("Số tiền tối đa là 100.000 vàng");
                            } else if (client.mChar.infoChar.vang < number) {
                                client.session.serivce.ShowMessGold("Không đủ vàng");
                            } else {
                                BauCuaTpl bauCuaTpl = new BauCuaTpl();
                                bauCuaTpl.idChar = client.mChar.id;
                                bauCuaTpl.cuoc = 1;
                                bauCuaTpl.cuocText = "TÔM";
                                bauCuaTpl.vangCuoc = number;
                                BauCua.gI().tongCuoc1 += number;
                                BauCua.gI().listChar.add(bauCuaTpl);
                                client.mChar.mineVang(number, true, true, "Đặt cược bầu cua");
                                client.session.serivce.closeTab();
                                client.mChar.zone.selectNpc(client, 11, 0, -1);
                            }
                        } catch (NumberFormatException e) {
                            client.session.serivce.ShowMessGold("Số tiền không hợp lệ");
                        }
                    } else {
                        client.session.serivce.ShowMessGold("Số tiền không hợp lệ");
                    }
                    break;
                case 5:
                    if (!BauCua.gI().isSetNumber) {
                        client.session.serivce.ShowMessGold("Đã hết thời gian đặt cược");
                        return;
                    }
                    if (BauCua.gI().checkPlayer(client.mChar.id) != null) {
                        client.session.serivce.ShowMessGold("Mỗi vòng chỉ được cược 1 lần");
                        return;
                    }
                    pattern = Pattern.compile("^\\d+$"); // ^ và $ đảm bảo toàn bộ chuỗi phải là số
                    matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        try {
                            int number = Integer.parseInt(input);
                            if (number < 10) {
                                client.session.serivce.ShowMessGold("Số tiền tối thiều là 10 vàng");
                            } else if (number > 100000) {
                                client.session.serivce.ShowMessGold("Số tiền tối đa là 100.000 vàng");
                            } else if (client.mChar.infoChar.vang < number) {
                                client.session.serivce.ShowMessGold("Không đủ vàng");
                            } else {
                                BauCuaTpl bauCuaTpl = new BauCuaTpl();
                                bauCuaTpl.idChar = client.mChar.id;
                                bauCuaTpl.cuoc = 2;
                                bauCuaTpl.cuocText = "CUA";
                                bauCuaTpl.vangCuoc = number;
                                BauCua.gI().tongCuoc2 += number;
                                BauCua.gI().listChar.add(bauCuaTpl);
                                client.mChar.mineVang(number, true, true, "Đặt cược bầu cua");
                                client.session.serivce.closeTab();
                                client.mChar.zone.selectNpc(client, 11, 0, -1);
                            }
                        } catch (NumberFormatException e) {
                            client.session.serivce.ShowMessGold("Số tiền không hợp lệ");
                        }
                    } else {
                        client.session.serivce.ShowMessGold("Số tiền không hợp lệ");
                    }
                    break;
            }
        } catch (Exception ex) {
            Utlis.logError(InputClient.class, ex, "Da say ra loi:\n" + ex.getMessage());
        }
    }

    public static double convertVNDToGold(double amount) {
        double rate;
        if (amount >= 2000000) {
            rate = 8.5;
        } else if (amount >= 1000000) {
            rate = 8.8;
        } else if (amount >= 500000) {
            rate = 9.0;
        } else if (amount >= 200000) {
            rate = 9.3;
        } else if (amount >= 100000) {
            rate = 9.6;
        } else if (amount >= 50000) {
            rate = 9.8;
        } else {
            rate = 10;
        }
        return amount / rate;
    }

}
