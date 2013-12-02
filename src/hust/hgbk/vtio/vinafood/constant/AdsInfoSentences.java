package hust.hgbk.vtio.vinafood.constant;

import hust.hgbk.vtio.vinafood.config.ServerConfig;

import java.util.Random;

public class AdsInfoSentences {
	private static String[] adsSentencesVn = {
		"Điểm Đến Việt - phần mềm dịch vụ thông tin du lịch, tiện ích cuộc sống tốt nhất.",
		"Điểm Đến Việt - Người hướng dẫn viên du lịch thông minh.",
		"Chúng tôi hiểu sở thích và đưa đến bạn những thông tin phù hợp nhất",
		"Điểm Đến Việt giúp bạn là thổ địa ở nơi mình mới đặt chân đến.",
		"Không lo bị lạc, còn biết cực nhanh, địa điểm xung quanh, có gì thú vị.",
		"Điểm Đến Việt: Tìm Khách Sạn, Nhà nghỉ mọi nơi, mọi lúc",
		"Bạn muốn được giới thiệu địa điểm ẩm thực theo style và vị trí của mình? Đã có Điểm Đến Việt",
		"Bạn muốn đưa con đi khám? Hãy để tôi giúp bạn tìm phòng khám nhi gần nhất",
		"Tối thứ bảy này đưa bạn gái đi đâu chơi? Hãy để  Điểm Đến  Việt giúp bạn",
		"Dạo chơi các bãi biển, khu du lịch, resort đơn giản với điện thoại của bạn. Chỉ có Điểm Đến Việt",
		"Muốn biết trạm ATM gần nhất mà không phải nhắn tin SMS có phí? Không thành vấn đề với  Điểm Đến  Việt.",
		"Xe hết xăng, hỏng hóc - để chúng tôi giúp bạn tìm trạm xăng, garage gần nhất.",
		"Hẹn hò offline - cà phê - karaoke - hãy là người khởi xướng với Điểm Đến Việt",
		"Điểm Đến Việt: Thông tin địa điểm phong phú - hình ảnh đẹp - bản đồ chỉ đường đến tận nơi.",
		"Điểm Đến Việt: Người bạn của tuổi teen với danh sách các quán kem, chè, trà sữa sành điệu nhất.",
		"Nửa đêm cần mua thuốc - hỏi Điểm Đến Việt",
		"Khám phá Đình Chùa Đền Miếu Nhà Thờ - công trình kiến trúc với Điểm Đến Việt",
		"Tìm địa điểm giải trí phù hợp? Quán Bar, Nhà Hát, Công Viên hay câu lạc bộ Billard? Không phần mềm nào tốt hơn Điểm Đến Việt"
		
	};
	private static String[] adsSentencesEn = {
		"Best tool for hanging out.",
		"Your intelligent iCompanion.",
		"Local business and Attraction in your hand.",
		"The best Viet Nam Travel Guide mobile software.",
		"Exploring your destination with Diem Den Viet"
	};
	
	public static String getRandomAdsSentence(){
		if (ServerConfig.LANGUAGE_CODE.equals("en")){
			int i = (new Random()).nextInt(adsSentencesEn.length);
			return adsSentencesEn[i];
		} else {
			int i = (new Random()).nextInt(adsSentencesVn.length);
			return adsSentencesVn[i];
		}
	}
}
