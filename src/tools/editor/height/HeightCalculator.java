package tools.editor.height;

/**
 * RS2 Client methods for calculating height.
 */
public class HeightCalculator {
	
	public static int[] COSINE = new int[2048];
	
	static {
		for(int i = 0; i < 2048; i++) {
			HeightCalculator.COSINE[i] = (int) (65536.0 * Math.cos(i * 0.0030679615));
		}
	}
	
	public static final int calculateVertexHeight(int i, int i_106_) {
		int mapHeight = HeightCalculator.method458(i + 45365, i_106_ + 91923, 4) - 128 + (HeightCalculator.method458(i + 10294, i_106_ + 37821, 2) - 128 >> 1) + (HeightCalculator.method458(i, i_106_, 1) - 128 >> 2);
		mapHeight = (int) (mapHeight * 0.3) + 35;
		if(mapHeight < 10) {
			mapHeight = 10;
		} else if(mapHeight > 60) {
			mapHeight = 60;
		}
		return mapHeight;
	}
	
	private static final int method458(int i, int i_140_, int i_141_) {
		int i_142_ = i / i_141_;
		int i_143_ = i & i_141_ - 1;
		int i_144_ = i_140_ / i_141_;
		int i_145_ = i_140_ & i_141_ - 1;
		int i_146_ = HeightCalculator.method468(i_142_, i_144_);
		int i_147_ = HeightCalculator.method468(i_142_ + 1, i_144_);
		int i_148_ = HeightCalculator.method468(i_142_, i_144_ + 1);
		int i_149_ = HeightCalculator.method468(i_142_ + 1, i_144_ + 1);
		int i_150_ = HeightCalculator.method466(i_146_, i_147_, i_143_, i_141_);
		int i_151_ = HeightCalculator.method466(i_148_, i_149_, i_143_, i_141_);
		return HeightCalculator.method466(i_150_, i_151_, i_145_, i_141_);
	}
	
	private static final int method466(int i, int i_211_, int i_212_, int i_213_) {
		int i_214_ = 65536 - HeightCalculator.COSINE[i_212_ * 1024 / i_213_] >> 1;
		return (i * (65536 - i_214_) >> 16) + (i_211_ * i_214_ >> 16);
	}
	
	private static final int method468(int i, int i_216_) {
		int i_217_ = HeightCalculator.calculateNoise(i - 1, i_216_ - 1) + HeightCalculator.calculateNoise(i + 1, i_216_ - 1) + HeightCalculator.calculateNoise(i - 1, i_216_ + 1) + HeightCalculator.calculateNoise(i + 1, i_216_ + 1);
		int i_218_ = HeightCalculator.calculateNoise(i - 1, i_216_) + HeightCalculator.calculateNoise(i + 1, i_216_) + HeightCalculator.calculateNoise(i, i_216_ - 1) + HeightCalculator.calculateNoise(i, i_216_ + 1);
		int i_219_ = HeightCalculator.calculateNoise(i, i_216_);
		return i_217_ / 16 + i_218_ / 8 + i_219_ / 4;
	}
	
	private static final int calculateNoise(int x, int seed) {
		int n = x + seed * 57;
		n = n << 13 ^ n;
		int noise = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
		return noise >> 19 & 0xff;
	}
}
