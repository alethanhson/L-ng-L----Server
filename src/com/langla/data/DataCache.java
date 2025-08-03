package com.langla.data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author PKoolVN
 **/
public class DataCache {
    public static short idbuyshop = 1;
    public static short baseIDZoneCustom = -1;

    public static short baseIDMob = -32000;

    public static short baseIDItemMap = -32000;

    public static List<List<Integer>> OptionCaiTrang = new ArrayList<List<Integer>>(Arrays.asList(
            Arrays.asList(0, 10, 101),
            Arrays.asList(1, 10, 101),
            Arrays.asList(2, 1, 55),
            Arrays.asList(3, 1, 55),
            Arrays.asList(4, 1, 55),
            Arrays.asList(5, 1, 55),
            Arrays.asList(6, 1, 2)
    ));
    public static List<List<Integer>> OptionCaiTrangVIP = new ArrayList<List<Integer>>(Arrays.asList(
            Arrays.asList(0, 100, 555),
            Arrays.asList(1, 100, 555),
            Arrays.asList(2, 100, 333),
            Arrays.asList(3, 100, 333),
            Arrays.asList(4, 100, 333),
            Arrays.asList(5, 100, 333),
            Arrays.asList(6, 1, 3)
    ));
    public static int[] dataDamePhanThan = new int[]{10,
    11,
    12,
    13,
    14,
    15,
    16,
    17,
    18,
    19,
    20,
    21,
    22,
    23,
    24,
    25,
    26,
    27,
    28,
    29,
    30,
    31,
    32,
    33,
    34,
    35,
    36,
    37,
    38,
    39,
    40};

    public synchronized static short getIDZoneCustom(){
        if (DataCache.baseIDZoneCustom < -32000) {
            return DataCache.baseIDZoneCustom = -1;
        }
        return DataCache.baseIDZoneCustom -= 1;
    }
    public synchronized static int getIDMob(){
        if (DataCache.baseIDMob > 32000) {
            return DataCache.baseIDMob = -32000;
        }
        return DataCache.baseIDMob += 1;
    }

    public synchronized static int getIDItemMap(){
        if (DataCache.baseIDItemMap > 32000) {
            return DataCache.baseIDItemMap = -32000;
        }
        return DataCache.baseIDItemMap += 1;
    }
    public static List<Integer> IdShop1 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 18, 19, 30, 37, 38, 39, 40);
    public static List<Integer> IdShop2 = Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 32, 33, 34, 35, 36);

    public static List<Integer> idMapCustom = Arrays.asList(46,47,84,89);

    public static List<Integer> idBoss = Arrays.asList(238);
    public static String[] camNang = new String[] {


            "Cần biết",
            "- Nếu cấp PK lờn hơn 0, mỗi ngày sẽ được giảm 1 cấp PK.\n- Nếu điểm chuyên cần lớn hơn 0 mỗi ngày sẽ bị trừ 1 điểm chuyên cần, nếu điểm chuyên cẩn từ 50 trở lên mỗi ngày sẽ bị trừ 2 điểm chuyền cần, Tương tự nếu điểm chuyên cần từ 100 trở lên mỗi ngày bị trừ 3 điểm chuyên cần...\n- Mỗi Ngày được chat tin thế giới miễn phí 20 lần (giới hạn thời gian mỗi lần chat là 1 phút), nếu chat nhiều hơn sẽ bị trừ 2 vàng khóa cho 1 ẩn chat tin thế giới (giới hạn thời gian mỗi lần chat là 30 giây) .",
            "Trạng thái chiến đấu",
            "- Cờ trắng: Đây là trạng thái hòa bình, Không thể chủ động chiến đầu với nhân vật khác.\n- cờ vàng: Những nhân vật đang ở trạng thái này, có thể chiến đấu với nhau mà không tăng cấp РК.\n- Cờ đỏ: Có thể chiến đấu với bất kỳ nhân vật nào nhưng sẽ bị tăng cấp PK khi đánh trọng thương nhân vật khác (đánh trọng thương 1 nhân vật sẽ bị tăng 1 cấp PK).\nNgoài ra ở chế độ truy sát sẽ làm tăng 2 cập PK khi dánh trọng thương nhân vật khác.",
            "Gia tộc",

            "c#greenThành lập gia tộc\n" +
                    "Cẩn đem theo Gía Tộc Lệnh đển gặp NPC Onoki để thành lập gia tộc. Sau khi thành lập gia tộc sẽ có được 30.000 bạc ngân sách để dùng chi tiêu trong gia tộc.\n" +
                    "c#greenThành viên và cấp gia tộc:\n" +
                    "Gia tộc cấp 1 sẽ thu nạp được tối đa 20 thành viên, cứ mỗi lần lên 1 cấp thì có thể thu nạp thêm 5 thành viên nữa. Các thành viên trong gia tộc có điểm công hiến càng nhiều thì cấp gia tộc càng lên nhanh.\n" +
                    "c#greenLợi ích khi có gia tộc\n" +
                    "- Được sử dụng các kỹ năng của gia tộc.\n" +
                    "- Được phân phát vật phẩm của gia tộc.\n" +
                    "- Mỗi ngày được tăng thêm 1 lần vượt ải gia tộc, Khi tham gia vượt ải gia tộc sẽ nhận được nhiều phần thưởng thú vị, Chỉ được tích lũy tối đa 5 lần vượt ải gia tộc\n" +
                    "- Mỗi tuần khi cống hiến đạt yêu cầu, gia tộc sẽ được tăng thêm bạc ngân sách và vàng khóa (cấp gia tộc càng cao thì phần thưởng sẽ càng nhiều). Ngược lại nếu cống hiến không đạt yêu cầu, ngân sách gia tộc sẽ bị giảm (cấp gia tộc càng cao sẽ bị giảm càng nhiều), nếu để ngân sách bằng 0 thì qua 0:00 ngày mới là gia tộc sẽ giải tán.\n" +
                    "c#greenPhân quyền trong gia tộc\n- Tộc trưởng, toàn quyền trong gia tộc (rút quỹ, phát lương, bổ nhiệm tộc phó..)\n- Tộc phó: được bổ nhiệm trưởng lão, mở cửa ải gia tộc, thông báo trong gia tộc, thu nạp thành viên, trục xuất thành viên, cấm chát, góp quỹ.\n- " +
                    "Trưỡng lão: thu nạp thánh vin, cấm Chat, góp quỹ.\nGhi chú: Mỗi lần bổ nhiệm, bãi chức, trục xuất thành viên sẽ bị trừ ngân sách.\n" +
                    "c#greenQuan hệ của gia tộc và thanh viên \n" +
                    "Thành viên: hoạt động để cống hiến xây dựng gia tộc, nếu bị đuổi hoặc tự rời gia tộc sẽ mắt hết điểm cống hiến.\n " +
                    "-Gia tộc: khi trục xuất thành viên vì 1 lý do nào đó, nếu điểm cống hiến của thành viên càng cao thì sẽ bị trừ ngân sách càng nhiều, nếu không đủ ngân sách sẽ không thể trục xuất thành viên.",
            "Nhiệm vụ tuần hoàn",
            "Sau khi nhân vật đạt cấp 10 trở lên, mỗi ngày có thể nhận nhiệm vụ tuần hoàn tại NPC Mei Terumi ở trong các làng, Sau khi hoàn thành hết 10 nhiệm vụ sẽ nhận được phần thưởng.",
            "Thu phục linh thú",
            "Sau khi nhân vật đạt cấp 15 trở lên, mỗi ngày có thể nhận nhiệm vụ thu phục lính thứ tại NPC Rasa ở trong các làng. Sau khi hoàn thành nhiệm vụ sẽ nhận được phần thưởng.",
            "Địa cung",
            "Sau khi nhân vật đạt cấp 15 trở lên, mỗi ngày có thể nhận cha khóa để đi vào địa cung (có thể tổ đội để cùng tham gia) tại NPC Raikage ở trong các làng. Sau khi hoàn thành khám phá địa cung sẽ nhận được phản thưởng.",
            "Cấm thuật Izanami",
            "Sau khi nhân vật đạt cấp 40 trở lên, mỗi ngày có thể sử dụng cảm thuật Izanami để đi vào vòng lặp ảo tưởng (có thể tổ đội để cùng tham gia) tại NPC Onoki ở trong các làng. Cứ mỗi lần vượt qua được nột vòng lặp ảo tưởng đều nhận Nược phần thưởng, vòng lặp càng cao quái và boss sẽ cảng mạnh. Thời gian chiến đầu trong mỗi vòng lặp là 5 phút và người chơi chỉ được hồi sinh tối đa 3 lẩn ở trong vòng lặp ảo tưởng.",
            "Vượt ải gia tộc",
            "Sau khi gia nhập gia tộc, mỗi ngày các thành viên trong gia tộc có thể cùng nhau tham gia vượt ải gia tộc tại NPC Onoki, Chỉ có tộc trưởng hoặc tộc phó được phép mở cửa ải gia tộc và chờ các thành viên cùng vào tham gia, sau khi hoán thánh vượt ải gia tộc sẽ nhận được nhiều phần thưởng.",
            "Đại chiến nhẫn giả lần III",
            "Sau khi nhân vật đạt cấp 15 trở lên, có thể báo danh tham gia Đại Chiến Nhân Giả Lần III chỉ diễn ra vào lúc 20h00-20h10 thứ 3, 5, 7 hàng tuần tại NPC Onoki.\nc#cyanLưu ý:\nCần phải đánh bể Long Trụ thì mới có thể gây sát thương lên Boss. Ngoài ra nếu đang ở phút lẻ thì mới có thể gây sát thượng lên Minato, phút chẵn thì mới có thể sát thương lên Onoki",
            "Đại hội nhẫn giả",
            "Sau khi nhân vật đạt cấp 15 trở lên, có thể báo danh tham gia Đại Hội Nhẫn Giả chỉ diễn ra vào lúc 20h20-20h30 thứ 2, 4, 6 hàng tuần tại NPC Onoki.",
            "Khu rừng chết",
            "Sau khi nhân vật đạt cấp 15 trở lên, mỗi ngày có thể báo danh tham gia Khu Rừng Chết vào lúc 6h50-7h, 9h50-10h, 12h50-13h, 15h50-15h, 18h5 Q- 19h tại Anko, Mỗi ngày chỉ có thẻ tham gia tối đa 2 lần vào Khu Rừng Chết.\nc#cyanLưu ý:\ncần phải có gia tộc thì khi dánh Boss mới rơi ra vật phẩm.",
            "Kiến thức nhẫn giả",
            "Mỗi ngày vào lúc 18h-18h45, NPC Senju Tsunade sẽ xuất hiện và đi lại trong trường Konoha, Người chơi có thể tìm đến và trả lời các câu hỏi của Tsunade. Sau khi trả lời hết 30 câu hỏi người chơi sẽ nhận được phần thưởng có giá trị tại NPC Ginkaku (phúc lợi quan).",
            "Cao thủ nhẫn giả",
            "Mỗi ngày vào lúc 9h, 14h, 19h, cao thủ nhân giả sẽ xuất hiện tại các map Thánh Địa Thất Kiếm, Đổi Hoang, Hang Khỉ, Chiến Trường Cổ, Rừng Nấm, Khu vực xuất hiện sẽ ngẫu nhiên từ 1-8, không cố định, người chơi cần phải đò tim ra khu vực của cao thủ nhẫn giả đang xuất hiện. Sau khi đánh thắng cao thủ nhân giả sẽ nhận được nhiều phần thưởng có giá trị. Nếu sau 1 giờ kẻ từ khi xuất hiện mà vẫn không có người dánh bại thì cao thử nhắn giả sẽ tự động biến mất.",
            "Câu cá cuối tuần",
            "Vào lúc 11h-11h30 chủ nhật hàng tuần sẽ có sự kiện câu cá cuối tuần diễn ra tại Làng Đá, Các nhẫn giả có thể nhận miễn phí 50 cần câu bạc tại NPC Ishikawa Kamizuru, ngoài ra các nhấn giả cũng có thể mua thêm cần câu bạc trong cửa tiệm tạp hóa, mua cần câu vàng trong cửa hàng khu vàng. Sử dụng cần câu bạc sẽ câu được các loại cá: cá trê, cá lóc, cá mè, cả ngát, cá hô, sử dụng cản câu vàng thì được thêm những loại cá quý hiếm, trong quá trình câu cá người chơi cũng sẽ có cơ hội nhận thêm các loại đá cấp 6, 7, 8 tùy theo độ may mắn. Khi kết thúc sự kiện thì nếu nhân vật đạt được Top 1-3 trong bảng xếp hạng câu cá sẽ nhận được phần thưởng có giá trị, ngoài ra các loại cá câu được khí sử dụng cũng sẽ nhận được phẩn thưởng.",
            "Gia tộc tranh đoạt lãnh thổ Vĩ Thú",
            "Vào lúc 17h-17h30 thứ tự và chủ nhật mỗi tuần, vĩ thú sẽ xuất hiện ngẩu nhiên các bãi đánh quái. Nơi nào có vì thú xuất hiện thì trạng thái chiến đấu của nhân vật sẽ chuyển sang cờ vàng. Gia tộc nảo thu phục được vĩ thú thì vùng lành thỗ nơi đó sẽ thuộc về gia tộc đó, Các thành viên trong gia tộc tập luyện tại lành thổ đó sẽ tăng thêm 10% exp và 5% bạc khóa, các nhân vật khác ngoài gia tộc thì bị trừ 10% exp. Khi vi thú xuất hiện lần tới tại vùng lãnh thỏ đó, thì vùng lãnh thổ đó sẽ không thuộc về gia tộc trước đó nữa, mà phải đi đánh chiếm lại.\nNgoài ra khi đánh bại vĩ thú gia tộc sẽ nhận được phần thưởng bạc khóa và chính vĩ thú đó (có HSD), 23h ngày cuối tháng các vùng lành thổ sẽ bị reset tại và gia tộc có được vùng lãnh thổ càng nhiều sẽ nhận được thêm nhiều phẩn thưởng, nếu được 3 Tành thổ trở lên thì nhận được thêm danh hiệu vô song. Lưu ý: vị dụ cấp 3x thì chỉ đánh được vĩ thú 3x, không thể đánh chênh lệch cấp quá nhiều. Ngoài ra nếu đang có bùa bảo hộ đánh vào vì thú sẽ bị mắt bùa bảo hộ.",
            "Đại hội nhẫn giả liên Server",
            "Vào lúc 20h20-20h30 chủ nhật hàng tuần sẽ điển ra giải đấu liên Server tại máy chủ Ngũ Kage. Các nhân giả chỉ cần có tên trên bảng xếp lạng lôi đải đều được phép đăng ký tham gia thi đầu, thời gian đăng ký tham gia từ thứ 2 đến thứ 7 hàng tuần tại NPC Onoki, sau khi đăng ký xong thì người chơi mới được đăng nhập váo máy chủ Ngũ Kage, Cần lưu ý, máy chủ Ngũ Kage sẽ không bảo lưu dữ liệu của người chơi, cứ mổi đầu tuần dừ liệu sẽ được xóa sạch, nếu muốn vào được máy chủ Ngũ Kagg thi người chơi sẽ phải đăng ký lại. Sau khi giải đầu liên Serer kết thúc người chơi có thể nhận phần thưởng ở tại NPC Onoki trong máy chủ thật mà mình đang chơi.",
            "Sơn cáp Myoboku",
            "Vào lúc 21h30-21h40 thứ 2,4, 6 hàng tuần sẽ mở cửa Sơn Sáp Myoboku, các nhân giả có thể tham gia hoạt động tại NPC Nhị Đại Hiền Nhân với nhiều phần thưởng vô cùng hấp dẫn.",
            "Làng Lá Plus",
            "Làng Lá Plus"


};
}
