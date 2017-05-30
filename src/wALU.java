import java.util.ArrayList;

/**
 * ģ��ALU���������͸���������������
 * @author 161250151_������
 *
 */

public class wALU {

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {
		//��sb1��32λ�Ķ�����������sb2��lengthλ�Ķ�������
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		int lengthOfInteger = 32;
		long mod = (long)Math.pow(2.0, lengthOfInteger);

		long value = Long.parseLong(number);
		//��ȡ32λ�Ķ�������
		if (value < 0) {
			//ֵС��0,��һ��ģϵͳ�и����ı�ʾ��������ģ��ֵ�ı�ʾһ��
			value = value + mod;
		}
		for(int i = lengthOfInteger - 1;i >= 0;i--){
			//��ͳһת����32λ�ٸ���length����������λ��
			if (value >= Math.pow(2.0, i)) {
				sb1.append(1);
				value = value - (long)Math.pow(2.0, i);
			}else{
				sb1.append(0);
			}
		}
		//��ȡ������չ
		if (length >= lengthOfInteger) {
			//��ȡ����λ����չ
			char SF = sb1.toString().charAt(0);
			if (SF == '0') {
				for(int i = 0;i<length - lengthOfInteger;i++){
					sb2.append(0);
				}
				sb2.append(sb1.toString());
			}else{
				for(int i = 0;i<length - lengthOfInteger;i++){
					sb2.append(1);
				}
				sb2.append(sb1.toString());
			}
		}else{
			//�ض�
			for(int j = lengthOfInteger - length;j < lengthOfInteger;j++){
				sb2.append(sb1.toString().charAt(j));
			}
		}
		String ret = sb2.toString();
		return ret;
	}

	/**
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		/* ��ȡ�÷���λ
		 * С����ǰ��Ĭ��Ϊ����ֵ
		 * ���������ö����Ʋ�����ʵ�֣��ӵ�һ����Ϊ0�ĵط���ʼappend
		 * */
		StringBuffer sb1 = new StringBuffer();
		double value = Double.parseDouble(number);
		//�������ֵ
		double pianyi = Math.pow(2.0, eLength-1)-1;
		double eMax = Math.pow(2.0, eLength)-1;
		double trueValueOfMaxE = eMax - pianyi;
		double max = Math.pow(2.0, trueValueOfMaxE) * (1 - Math.pow(2.0, -sLength-1));
		//������Сֵ
		double min = Math.pow(2.0, 1-pianyi) * Math.pow(2.0, -sLength);
		//һЩ�������
		if(value == 0){
			//����0ʱ
			for(int i = 0;i < eLength + sLength + 1;i++){
				sb1.append(0);
			}
		}else if(value > max){
			//���������0��ָ��ȫ1,β��ȫ0
			sb1.append(0);
			for(int i = 0;i < eLength;i++){
				sb1.append(1);
			}
			for(int i = 0;i < sLength;i++){
				sb1.append(0);
			}
		}else if(value < -max){
			//���������1��ָ��ȫ1,β��ȫ0
			sb1.append(1);
			for(int i = 0;i < eLength;i++){
				sb1.append(1);
			}
			for(int i = 0;i < sLength;i++){
				sb1.append(0);
			}
		}else if(value < min && value > 0){
			//�ǹ������
			sb1.append(0);
			for(int i = 0;i < eLength;i++){
				sb1.append(0);
			}
			for(int i = 0;i < sLength;i++){
				if (value >= Math.pow(2.0, -1-i)) {
					sb1.append(1);
					value = value - Math.pow(2.0, -1-i);
				}else{
					sb1.append(0);
				}
			}
		}else if(value < 0 && value > -min){
			//�ǹ�񻯸���
			sb1.append(1);
			for(int i = 0;i < eLength;i++){
				sb1.append(0);
			}
			for(int i = 0;i < sLength;i++){
				if (value >= Math.pow(2.0, -1-i)) {
					sb1.append(1);
					value = value - Math.pow(2.0, -1-i);
				}else{
					sb1.append(0);
				}
			}
		}
		else {
			//�����
			if (value < 0) {
				//��������λ��1
				sb1.append(1);
				value = Math.abs(value);
			}else{
				//��������λ��0
				sb1.append(0);
			}
			long integerPart = (long)value;
			double decimalsPart = value - integerPart;
			//��ȡ�������ֵĶ�����
			String integerPartBase2 = Long.toBinaryString(integerPart);
			if (value >= 1) {
				//���ڵ���1��ʱ������
				int lengthOfIntegerPart = integerPartBase2.length();//��λ�ض�Ϊ1������λ��Ϊ���������Ʊ�ʾ�ĳ���-1
				int currentE = lengthOfIntegerPart - 1 + (int)pianyi;
				sb1.append(integerRepresentation(Integer.toString(currentE), eLength));//��ӽ���
				sb1.append(integerPartBase2.substring(1));//����λ���أ��Ѿ������length-1λ��С����ʾ��Ҫ��ȥ��ô��λ
				for (int i = 0; i < sLength - lengthOfIntegerPart + 1; i++) {
					if (decimalsPart >= Math.pow(2.0, -1-i)) {
						sb1.append(1);
						decimalsPart = decimalsPart - Math.pow(2.0, -1-i);
					}else{
						sb1.append(0);
					}
				}
			}else{
				//С��1����0��ʱ������
				int count = 0;//count��¼��Ҫ���Ƶ�λ��
				loop:			for(int i = 0;i < sLength;i++){
					while(decimalsPart >= Math.pow(2.0, -1-i)){
						count = i + 1;
						break loop;
					}
				}
				StringBuffer decimalCollect = new StringBuffer();
				for(int i= 0;i < sLength + count + 1;i++){
					if (decimalsPart >= Math.pow(2.0, -1-i)) {
						decimalCollect.append(1);
						decimalsPart = decimalsPart - Math.pow(2.0, -1-i);
					}else{
						decimalCollect.append(0);
					}
				}
				int currentE = (int)pianyi - count;
				sb1.append(integerRepresentation(Integer.toString((int)currentE), eLength));//��ӽ���,������չ�ɽ��볤��λ
				sb1.append(decimalCollect.toString().substring(count,sLength + count));//���Ҫ��ȫβ������

			}

		}
		return sb1.toString();
	}

	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int) floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754 (String number, int length) {
		String output = null;
		if(length == 32){
			output = floatRepresentation(number, 8, 23);
		}else if(length == 64){
			output = floatRepresentation(number, 11, 52);
		}
		return output;
	}

	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue (String operand) {
		//�ж�����
		char SF = operand.charAt(0);
		int lengthOfOperand = operand.length();
		int sum = 0;
		if (SF == '0') {
			//����ֱ�Ӽ���
			for(int i = 0;i < lengthOfOperand;i++){
				if (operand.charAt(i) == '1') {
					sum = sum + (int)Math.pow(2.0, lengthOfOperand - 1 - i);
				}
			}
		}else{
			//������ȡ��
			StringBuffer sb1 = new StringBuffer();
			for(int i = 0;i < lengthOfOperand;i++){
				if (operand.charAt(i) == '0') {
					sb1.append(1);
				}else{
					sb1.append(0);
				}
			}
			String afterChange = sb1.toString();
			for(int i = 0;i < afterChange.length();i++){
				if (afterChange.charAt(i) == '1') {
					sum = sum + (int)Math.pow(2.0, afterChange.length() - 1 - i);
				}
			}
			//ȡ��֮���ټ���1
			sum = sum +1;
			sum = -sum;
		}
		return Integer.toString(sum);
	}

	/**
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		char SF = operand.charAt(0);//��ȡ����λ
		String e = operand.substring(1, eLength + 1);//��ȡ����
		String s = operand.substring(eLength + 1, operand.length());//��ȡβ��

		//��ͨ�������ֵ�ж��Ƿ����������
		int valueOfE = 0;//�������ֵ
		int pianyi = (int)Math.pow(2.0, eLength-1) - 1;//ƫ�ó���Ϊ2�����λ�η���һ
		for(int i = 0;i < eLength;i++){
			valueOfE = valueOfE + Integer.parseInt(String.valueOf(e.charAt(i))) * (int)Math.pow(2.0, eLength - 1 - i );
		}
		StringBuffer sb1 = new StringBuffer();
		for(int i = 0;i < sLength;i++){
			sb1.append(0);
		}
		String allzero = sb1.toString();
		if (valueOfE == 0) {
			//0��ǹ����
			if (s.equals(allzero)) {
				return "0";
			}else{
				double sum = 0.0;
				for(int i = 0;i < sLength;i++){
					sum = sum +  Integer.parseInt(String.valueOf(s.charAt(i))) * Math.pow(2.0, -1-i);
				}
				sum = sum * Math.pow(2.0, 1-pianyi);
				if (SF == '1') {
					sum = -sum;
				}
				return Double.toString(sum);
			}
		}else if(valueOfE-pianyi == (int)Math.pow(2.0, eLength)-1){
			//���������NaN
			if (!s.equals(allzero)) {
				//NaN����ȫ1��β����0
				return "NaN";
			}else{
				//�������ȫ1��β��Ϊ0������������λ
				if (SF == '0') {
					return "+Inf";
				}else{
					return "-Inf";
				}
			}
		}else {
			//�����
			int trueValueOfE = valueOfE - pianyi;
			double sum = 0.0;
			for(int i = 0;i < sLength;i++){
				sum = sum +  Integer.parseInt(String.valueOf(s.charAt(i))) * Math.pow(2.0, -1-i);
			}
			sum = sum + 1;//ǰ�汻���ص�1Ҫ����ȥ
			sum = sum * Math.pow(2.0, trueValueOfE);
			if (SF == '1') {
				sum = -sum;
			}
			return Double.toString(sum);
		}
	}

	/**
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation (String operand) {
		int lengthOfOperand = operand.length();
		StringBuffer sb1 = new StringBuffer();
		for(int i = 0;i<lengthOfOperand;i++){
			if (operand.charAt(i) == '0') {
				sb1.append(1);
			}else{
				sb1.append(0);
			}
		}
		String output = sb1.toString();
		return output;
	}

	/**
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
	 */
	public String leftShift (String operand, int n) {
		//����nλ�൱�ڰ����nλ��ֵ�����������油0
		int lengthOfOperand = operand.length();
		StringBuffer sb1 = new StringBuffer();
		if (n >= lengthOfOperand) {
			//ȫ��û��
			for(int i = 0;i < lengthOfOperand;i++){
				sb1.append(0);
			}
		}else{
			for(int i = n;i < lengthOfOperand;i++){
				sb1.append(operand.charAt(i));
			}
			for(int i = 0;i < n;i++){
				sb1.append(0);
			}
		}

		String output = sb1.toString();
		return output;
	}

	/**
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
	 */
	public String logRightShift (String operand, int n) {
		//����nλ�൱�ڰ��ұ�nλ����
		int lengthOfOperand = operand.length();
		StringBuffer sb1 = new StringBuffer();
		if (n >= lengthOfOperand) {
			//ȫ��û��
			for(int i = 0;i < lengthOfOperand;i++){
				sb1.append(0);
			}
		}else{
			for(int i = 0;i < n;i++){
				sb1.append(0);
			}
			for(int i = 0;i < lengthOfOperand - n;i++){
				sb1.append(operand.charAt(i));
			}
		}
		String output = sb1.toString();
		return output;
	}

	/**
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		//��������nλ��������λ��nλ�������ӷ���λ
		char SF = operand.charAt(0);
		int lengthOfOperand = operand.length();
		StringBuffer sb1 = new StringBuffer();
		if (n >= lengthOfOperand) {
			//ȫ��û��
			for(int i = 0;i < lengthOfOperand;i++){
				sb1.append(SF);
			}
		}else{
			for(int i = 0;i < n;i++){
				sb1.append(SF);
			}
			for(int i = 0;i < lengthOfOperand - n;i++){
				sb1.append(operand.charAt(i));
			}
		}
		String output = sb1.toString();
		return output;
	}

	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder (char x, char y, char c) {
		StringBuffer sb1 = new StringBuffer();
		//�Ȱ������ַ�ת��������
		int valueOfX = Integer.parseInt(String.valueOf(x));
		int valueOfY = Integer.parseInt(String.valueOf(y));
		int valueOfC = Integer.parseInt(String.valueOf(c));
		int current = valueOfX ^ valueOfY ^ valueOfC;
		int ahead = (valueOfX&valueOfC) | (valueOfY&valueOfC) | (valueOfX&valueOfY);
		sb1.append(Integer.toString(ahead));
		sb1.append(Integer.toString(current));
		String result = sb1.toString();
		return result;
	}

	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder (String operand1, String operand2, char c) {
		//��ȡ��������������λ�ϵ�ֵ���ɵ͵��߷ֱ�Ϊ1234
		int x1 = Integer.parseInt(String.valueOf(operand1.charAt(3)));
		int x2 = Integer.parseInt(String.valueOf(operand1.charAt(2)));
		int x3 = Integer.parseInt(String.valueOf(operand1.charAt(1)));
		int x4 = Integer.parseInt(String.valueOf(operand1.charAt(0)));
		int y1 = Integer.parseInt(String.valueOf(operand2.charAt(3)));
		int y2 = Integer.parseInt(String.valueOf(operand2.charAt(2)));
		int y3 = Integer.parseInt(String.valueOf(operand2.charAt(1)));
		int y4 = Integer.parseInt(String.valueOf(operand2.charAt(0)));

		//P�Ĺ�ʽ��P = X + Y���ӷ��ɻ����ʵ��
		int p1 = x1|y1;
		int p2 = x2|y2;
		int p3 = x3|y3;
		int p4 = x4|y4;

		//G�Ĺ�ʽ��G = XY���˷��������ʵ��
		int g1 = x1&y1;
		int g2 = x2&y2;
		int g3 = x3&y3;
		int g4 = x4&y4;

		//���ݹ�ʽ�õ�c1 c2 c3 c4
		int c0 = Integer.parseInt(String.valueOf(c));
		//C1 = G1 + P1*C0
		int c1 = g1|(p1&c0);
		//C2 = G2 + P2*G1 + P2*P1*C0
		int c2 = g2|(p2&g1)|(p2&p1&c0);
		//C3 = G3 + P3*G2 + P3*P2*G1 + P3*P2*P1*C0
		int c3 = g3|(p3&g2)|(p3&p2&g1)|(p3&p2&p1&c0);
		//C4 = G4 + P4*G3 + P4*P3*G2 + P4*P3*P2*G1 + P4*P3*P2*P1*C0
		int c4 = g4|(p4&g3)|(p4&p3&g2)|(p4&p3&p2&g1)|(p4&p3&p2&p1&c0);

		//�����λ���Ľ��,result1234��ʾ
		char r1 = fullAdder(operand1.charAt(3), operand2.charAt(3), c).charAt(1);
		char r2 = fullAdder(operand1.charAt(2), operand2.charAt(2), Integer.toString(c1).charAt(0)).charAt(1);
		char r3 = fullAdder(operand1.charAt(1), operand2.charAt(1), Integer.toString(c2).charAt(0)).charAt(1);
		char r4 = fullAdder(operand1.charAt(0), operand2.charAt(0), Integer.toString(c3).charAt(0)).charAt(1);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(Integer.toString(c4).charAt(0));
		sb1.append(r4);
		sb1.append(r3);
		sb1.append(r2);
		sb1.append(r1);
		String output = sb1.toString();
		return output;
	}

	/**
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder (String operand) {
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		ArrayList<Integer> storec = new ArrayList<>();
		int value = 0;
		int c = 1;//c��ʾ��λ,c0��1��ֵ
		storec.add(c);
		int f = 0;//f��ʾ��һλ�ӷ�����Ľ��
		for(int i = operand.length()-1;i >= 0;i--){
			value = Integer.parseInt(String.valueOf(operand.charAt(i)));
			f = c^value;
			c = c&value;
			storec.add(c);
			sb1.append(f);
		}
		int overfolw = storec.get(storec.size()-1)^storec.get(storec.size()-2);
		sb2.append(overfolw);
		//���ڽ���ǵ������ģ�����Ҫ��sb1������ȫ������
		String result = sb1.toString();
		for(int i = result.length() - 1;i >= 0;i--){
			sb2.append(result.charAt(i));
		}
		String output = sb2.toString();
		return output;
	}

	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		//�ȼ�����λ�ӷ�������Ҫ�Ĵ�������������������չ����ѭ�����
		int times = length / 4;
		String add1 = null;
		String add2 = null;
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		//����������չ
		for(int i = 0;i < length - operand1.length();i++){
			sb1.append(operand1.charAt(0));
		}
		sb1.append(operand1);
		add1 = sb1.toString();
		//��������չ
		for(int i = 0;i < length - operand2.length();i++){
			sb2.append(operand2.charAt(0));
		}
		sb2.append(operand2);
		add2 = sb2.toString();
		//������λ�ӷ���ѭ����ӣ���õĽ��4λ4λ���������
		StringBuffer sb3 = new StringBuffer();//�����Ҵ���ӵ͵��ߵ�������
		StringBuffer sb4 = new StringBuffer();
		ArrayList<Character> storec = new ArrayList<>();//����ÿһ����λ�ӷ�ÿ�εĽ�λ��Ϣ�������ж����
		for(int i = 0;i < times ;i++){
			String partOfAdd1 = add1.substring(add1.length() - 4 - 4 * i,add1.length() - 4 * i);
			String partOfAdd2 = add2.substring(add2.length() - 4 - 4 * i,add2.length() - 4 * i);
			String sum = claAdder(partOfAdd1, partOfAdd2, c);
			sb3.append(sum.substring(1, 5));
			c = sum.charAt(0);
			storec.add(c);
		}
		ArrayList<Integer> storeCfinal = new ArrayList<>();
		int Cfinal = Integer.parseInt(String.valueOf(storec.get(storec.size()-1)));
		for(int i = 0;i < add1.substring(0,4).length();i++){
			Cfinal = Cfinal&Integer.parseInt(String.valueOf(add1.substring(0, 4).charAt(3-i))) |
					Cfinal&Integer.parseInt(String.valueOf(add2.substring(0, 4).charAt(3-i))) |
					Integer.parseInt(String.valueOf(add1.substring(0, 4).charAt(3-i)))&Integer.parseInt(String.valueOf(add2.substring(0, 4).charAt(3-i)));
			storeCfinal.add(Cfinal);
		}
		int overfolw = storeCfinal.get(storeCfinal.size()-1)^storeCfinal.get(storeCfinal.size()-2);
		sb4.append(overfolw);
		for(int i = 0;i < times ;i++){
			sb4.append(sb3.toString().substring(sb3.toString().length()- 4 - 4 * i, sb3.toString().length() - 4 * i));
		}
		return sb4.toString();
	}

	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}

	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		//��ȥһ�������ڼ���������Ĳ��룬��ȡ�ü�������ֵ����ȡ������ȡ�ö����������ٵ��üӷ�
		int trueValueOfOP2 = Integer.parseInt(integerTrueValue(operand2));
		String afterChange = integerRepresentation(Integer.toString(-trueValueOfOP2), length);
		String output = integerAddition(operand1, afterChange, length);
		return output;
	}

	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		String x = operand1;//x�Ǳ�����
		String y = operand2;//y�ǳ���
		char helper = '0';//helper�����˸���λ
		String p = "";//p��ʾ���ֻ�
		for(int i = 0;i < length;i++){
			p = p + "0";
		}
		//��չx��y
		int xToAdd = length - x.length();
		if (x.length() < length) {
			char SF = x.charAt(0);
			for(int i = 0; i < xToAdd; i++){
				x = SF + x;
			}
		}
		int yToAdd = length - y.length();
		if (y.length() < length) {
			char SF = y.charAt(0);
			for(int i = 0; i < yToAdd; i++){
				y = SF + y;
			}
		}
		//һ��Ҫ����length�ε���������
		char overflow = '0';
		for(int i = 0;i < length;i++){
			//����00 01 10 11�ĸ���ͬ����ж�Ҫ���еĲ���
			if (y.charAt(y.length()-1) == '0') {
				if (helper == '1') {
					//01���ӷ�
					p = integerAddition(p, x, length).substring(1);
					overflow = integerAddition(p, x, length).charAt(0);

				}
			}else{
				if (helper == '0') {
					//10������
					p = integerSubtraction(p, x, length).substring(1);
					overflow = integerAddition(p, x, length).charAt(0);
				}
			}

			//��������
			helper = y.charAt(length - 1);
			y = p.charAt(p.length()-1) + y.substring(0,y.length()-1);
			p = ariRightShift(p, 1);
		}
		String output = overflow + y;
		return output;
	}

	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		char SFofX = operand1.charAt(0);//x��ʾ������
		char SFofY = operand2.charAt(0);//y��ʾ����
		String x = operand1;
		String y = operand2;
		//������չ��lengthλ
		if (operand1.length() < length) {
			for(int i = 0;i < length - operand1.length();i++){
				x = SFofX + x;
			}
		}
		if (operand2.length() < length) {
			for(int i = 0;i < length - operand2.length();i++){
				y = SFofY + y;
			}
		}
		//���������Ĵ���R
		String r = "";
		String q = x;
		char qn = '0';
		for(int i = 0;i < length;i++){
			r = SFofX + r;
		}
		//���һλ�̣���XYͬ��R1 = X - Y���������ӷ�
		if (SFofX != SFofY) {
			r = integerAddition(x, y, length).substring(1);
		}else{
			r = integerSubtraction(x, y, length).substring(1);
		}
		//��R1��Yͬ�ţ�Q��1,������0
		if (r.charAt(0) == SFofY) {
			qn = '1';
		}else{
			qn = '0';
		}
		char overflow = '0';
		if ((SFofX==SFofY && qn=='1') || (SFofX!=SFofY && qn == '0')) {
			overflow = '1';
		}
		//����
		r = r.substring(1) + q.substring(0, 1);
		q = q.substring(1) + qn;
		//����length ��ѭ��
		//R��Yͬ�ţ�Q��n-1����1��R��n+1��=2Rn-Y
		//R��Y��ţ�Q��n-1����0��R��n+1��=2Rn+Y
		for(int i = 0;i < length ;i++){
			if (qn == '1') {
				r = integerSubtraction(r, y, length).substring(1);
			}else{
				r = integerAddition(r, y, length).substring(1);
			}
			if (r.charAt(0) == SFofY) {
				qn = '1';
			}else{
				qn = '0';
			}
			r = r.substring(1) + q.substring(0, 1);
			q = q.substring(1) + qn;
		}

		//�̵�����
		if (SFofX == SFofY) {
			//������ͬ����ʱ��������������
		}else{
			//���Ų�ͬ���̼���1
			q = oneAdder(q).substring(1);
		}
		//��������
		if(r.charAt(0) == SFofX){
			//����Ҫ����
		}else{
			if (SFofX == SFofY) {
				r = integerAddition(r, y, length).substring(1);
			}else{
				r = integerSubtraction(r, y, length).substring(1);
			}
		}
		String output = overflow + q + r;
		return output;
	}

	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int) integerAddition}��
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		//ȡ�÷���
		char SFofX = operand1.charAt(0);
		char SFofY = operand2.charAt(0);
		//ȡ����ֵ
		String x = operand1.substring(1);
		String y = operand2.substring(1);
		//��չ
		if (x.length() < length) {
			int xToAdd = length - x.length();
			for(int i = 0;i < xToAdd;i++){
				x = "0" + x;
			}
		}
		if (y.length() < length) {
			int yToAdd = length - y.length();
			for(int i = 0;i < yToAdd;i++){
				y = "0" + y;
			}
		}
		//ͨ������ж��Ƿ�Ϊͬ�ţ�ͬ����ͣ�������
		String output = null;
		if ((SFofX == '0' && SFofY == '0') || (SFofX == '1' && SFofY == '1')) {
			String sum = adder(x, y, '0', length);
			output = sum.substring(0, 1)+ SFofX + sum.substring(1);
		}else{
			String sum = adder(x, y, '0', length);
			if (sum.charAt(0) == '1') {
				String negation = negation(sum.substring(1));
				String adddone = oneAdder(negation);
				output = negation(String.valueOf(SFofX)) + adddone;
			}else{
				output = sum.substring(0, 1)+ SFofX + sum.substring(1);
			}
		}
		return output;
	}

	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		//���������������0����չ
		String output = null;
		String x = operand1.substring(1+eLength, 1+eLength+sLength);
		String y = operand2.substring(1+eLength, 1+eLength+sLength);
		for(int i = 0;i < gLength;i++){
			x = x + "0";
			y = y + "0";
		}
		//�Խ�
		String eOfX = integerTrueValue(operand1.substring(1, 1+eLength));
		String eOfY = integerTrueValue(operand2.substring(1, 1+eLength));
		String eOfBoth = null;
		int delta = Integer.parseInt(eOfX) - Integer.parseInt(eOfY);
		if (delta >= 0) {
			//x�Ľ����y�Ľ����,y�߼�����
			y = logRightShift(y,delta);
			eOfBoth = eOfX;
		}else{
			//y�Ľ����x�ĸ�
			x = logRightShift(x, Math.abs(delta));
			eOfBoth = eOfY;
		}
		String sum = signedAddition(operand1.charAt(0)+x, operand2.charAt(0)+y, sLength+gLength);
		//�ж�sum�Ƿ�Ϊ0
		String allzero = null;
		for(int i = 0 ;i < sLength + gLength - 2;i++){
			allzero = "0" + allzero;
		}
		if (sum.substring(2).equals(allzero)) {
			//sumΪ0
			for (int i = 0; i < 2 + sLength + eLength; i++) {
				output = "0" + output;
			}
			return  output;
		}else{
			//sum��Ϊ0
			//�ж��Ƿ����
			if (sum.charAt(1) == '1') {
				//������ҹ�
				eOfBoth = integerRepresentation(Integer.toString(Integer.parseInt(integerTrueValue(eOfBoth)) + 1), eLength);
				sum = logRightShift(sum, 1);
			}else{
				//����������
				int count = 0;
				countloop:			for(int i = 2;i < 2+sLength+gLength;i++){
					if (sum.charAt(i) == '0') {
						count++;
					}else{
						break countloop;
					}
				}
				eOfBoth = integerRepresentation(Integer.toString(Integer.parseInt(integerTrueValue(eOfBoth)) - count), eLength);
				sum = leftShift(sum, count);
			}
			//ֱ�ӱ����ض�
			sum = sum.substring(2, 2+sLength);
		}
		//�ж��Ƿ����
		String allone = "";
		for(int i = 0;i<eLength;i++){
			allone = "1" + allone;
		}
		String result = "";
		if (eOfBoth.equals(allone)) {
			//����
			result = "1" + result;
		}else{
			result = "0" + result;
		}
		result = result + (operand1.charAt(0)^operand2.charAt(0)) + eOfBoth + sum;
		return result;
	}

	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int) floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		String qufan = negation(operand2.substring(eLength+1));
		String addone = oneAdder(qufan).substring(1);
		String output = floatAddition(operand1, operand2.substring(0, eLength+1)+addone, eLength, sLength, gLength);
		return output;
	}

	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int) integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}

	/**
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
}