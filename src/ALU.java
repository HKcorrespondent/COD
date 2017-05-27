import java.util.Arrays;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author 161250170_徐翔哲
 *
 */

public class ALU {

	private String showBits(int a,int len){
		StringBuffer buffer=new StringBuffer();
		int temp;
		int mask=1<<31;
		a<<=32-len;
		if(a==0){
			buffer.append("0");
		}else {
			while(a!=0){
				temp=(a&mask)>>>31;
				buffer.append(String.valueOf(temp));
				a<<=1;
			}
		}
		if(buffer.length()<len){
			int diff=len-buffer.length();
			for(;diff>0;diff--){
				buffer.append("0");
			}
		}else if(buffer.length()>len){
			buffer.setLength(len);
		}
		return buffer.toString();
	}

	private int power2(int n){
		return n<32?1<<n:0;
	}




	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {
		int a=Integer.parseInt(number);
		String ret;
		if(a>0){
			ret=integerToBinary(number);
		}else {
			int power2=power2(length);
			ret=showBits(power2+a,length);
		}
		return ret;
	}


	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		char[] ret=new char[eLength+sLength+1];
		Arrays.fill(ret,'0');
		//deal with inf
		if(number.equals("+Inf")){
			for(int i=0;i<eLength;i++){
				ret[i+1]='1';
			}
			return new String(ret);
		}else if(number.equals("-Inf")){
			for(int i=0;i<=eLength;i++){
				ret[i]=1;
			}
			return new String(ret);
		}


		char[] srcInt=number.split(".")[0].toCharArray();
		char[] srcDec=number.split(".")[1].toCharArray();
		boolean sig=true;//   + -->true     - -->false;
		//determine the signal of result
		if(srcInt[0]=='-'){
			ret[0]='1';
			sig=false;
			srcInt=Arrays.copyOfRange(srcInt,1,srcInt.length);
		}else {
			ret[0]='0';
		}

		char[] srcBint=integerToBinary(new String(srcInt)).toCharArray();
		char[] srcBdec=decimal2Binary(new String(srcDec),sLength).toCharArray();

        //deal with too big number
        int eMax=(1<<(eLength-1))-1;
        if(srcBint.length-1>eMax){
            return sig?floatRepresentation("+Inf",eLength,sLength):floatRepresentation("-Inf",eLength,sLength);
        }
        //deal pure decimal
        if(srcBint.length==0){
            int cnt=0;
			for (char aSrcBdec : srcBdec) {
				if (aSrcBdec == 1) {
					cnt++;
					break;
				}
				cnt++;
			}
            if(cnt>=eMax){
				//exp-underflow
				char[] retDec;
                char[] zero=new char[cnt-eMax];
                Arrays.fill(zero,'0');
                retDec=(new String(zero)+new String(srcBdec)).toCharArray();
                for(int i=0;i<eLength;i++){
                	ret[1+i]='0';
				}
				for(int i=0;i<sLength;i++){
                	ret[1+eLength+i]=retDec[i];
				}
				return new String(ret);
            }else {
            	//formal
            	char[] exp=integerToBinary(String.valueOf(eMax-cnt)).toCharArray();
            	for(int i=0;i<exp.length;i++){
            		ret[1+eLength-1-i]=exp[exp.length-1-i];
				}
				for(int i=exp.length;i<eLength;i++){
            		ret[1+eLength-1-i]='0';
				}
				System.arraycopy(srcBdec,0,ret,1+eLength,sLength);
//				for(int i=0;i<sLength;i++){
//					ret[1+eLength+i]=srcBdec[i];
//				}
				return new String(ret);
			}
		}

		//formal
		int cnt=0;//record the position of the first 1
		for(int i=0;;i++){
			cnt++;
			if(srcBint[i]=='1'){
				break;
			}
		}
		char[] srcB=(new String(srcBint)+new String(srcBdec)).toCharArray();
		char[] eTemp=integerToBinary(String.valueOf(eMax+cnt)).toCharArray();
		if(eTemp.length<eLength){
			ret[1]='0';
			System.arraycopy(eTemp, 0, ret, 2, eLength - 1);
		}else {
			System.arraycopy(eTemp,0,ret,1,eLength);
		}

		System.arraycopy(srcB,cnt,ret,2+eLength,sLength);


		return new String(ret);
	}
	
	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {

		return null;
	}
	
	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		StringBuffer buffer=new StringBuffer();
		char temp;
		for(int i=0,len=operand.length();i<len;i++){
			temp=operand.charAt(i);
			if(temp=='0'){
				buffer.append('1');
			}else {
				buffer.append('0');
			}
		}
		return new String(buffer);
	}
	
	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift (String operand, int n) {
		StringBuffer zerobuf=new StringBuffer();
		for(int i=1;i<=n;i++){
			zerobuf.append('0');
		}
		operand=operand+new String(zerobuf);

		return operand.substring(n);
	}
	
	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift (String operand, int n) {
		int len=operand.length();
		StringBuffer zerobuf=new StringBuffer();
		for(int i=1;i<=n;i++){
			zerobuf.append('0');
		}
		operand=new String(zerobuf)+operand;
		return operand.substring(0,len);
	}
	
	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift (String operand, int n) {
		char first=operand.charAt(0);
		if(first=='1'){
			int len=operand.length();
			StringBuffer sigbuffer=new StringBuffer();
			for(int i=1;i<=n;i++){
				sigbuffer.append('1');
			}
			operand=new String(sigbuffer)+operand;
			return operand.substring(0,len);
		}else {
			return logRightShift(operand,n);
		}
	}
	
	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder (char x, char y, char c) {
		char first=(char)(((x&y)|(c&(x^y)))&0x0f+0x30);
		char second=(char)(x^y^c);
		return ""+first+second;
	}
	
	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {
		char[] num1=operand1.toCharArray();
		char[] num2=operand2.toCharArray();
		char[] g=new char[4];
		char[] p=new char[4];
		char[] carry=new char[5];
		char[] ret=new char[5];
		carry[4]=c;
		for(int i=0;i<4;i++){
			p[3-i]=(char)(((num1[i]|num2[i])&0xf)|0x30);
			g[3-i]=(char)((num1[i]&num2[i]&0xf)|0x30);
		}
		carry[3]=(char)(g[0]|(p[0]&c));
		carry[2]=(char)(g[1]|(p[1]&g[0])|(p[1]&p[0]&c));
		carry[1]=(char)(g[2]|(p[2]&g[1])|(p[2]&p[1]&g[0])|(p[2]&p[1]&p[0]&c));
		carry[0]=(char)(g[3]|(p[3]&g[3])|(p[3]&p[2]&g[1])|(p[3]&p[2]&p[1]&g[0])|(p[3]&p[2]&p[1]&p[0]&g[0]));

		ret[0]=carry[0];
		for(int i=1;i<5;i++){
			ret[i]=fullAdder(num1[i-1],num2[i-1],carry[i]).charAt(1);
		}
		return new String(ret);
	}
	
	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		int len=operand.length();
		char[] ret=new char[len+1];
		char[] num1=operand.toCharArray();
		char[] num2=new char[len+1];
		Arrays.fill(num2,'0');
		num2[len]='1';
		char[] carry=new char[len+1];
		Arrays.fill(carry,'0');
		for(int i=len-1;i>=0;i--){
			ret[i+1]=(char)(num1[i]^num2[i+1]^carry[i+1]);
			carry[i]=(char)((num1[i]&carry[i+1])|(num2[i+1]&carry[i+1])|(num1[i]&num2[i+1]));
		}
		ret[0]=(char)((carry[1]^carry[0])+'0');
		return new String(ret);
	}
	
	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		char[] num1=operand1.toCharArray();
		char[] num2=operand2.toCharArray();

		//符号扩展
		StringBuffer sigbuffer=new StringBuffer();
		if(num1.length<length){
			int bias=length-num1.length;
			char sig=num1[0];
			for(int i=bias;i>0;i--){
				sigbuffer.append(sig);
			}
			num1=(new String(sigbuffer)+operand1).toCharArray();
			sigbuffer.setLength(0);
		}else if (num2.length<length){
			int bias=length-num2.length;
			char sig=num2[0];
			for(int i=bias;i>0;i--){
				sigbuffer.append(sig);
			}
			num2=(new String(sigbuffer)+operand2).toCharArray();
		}
		//保存符号以备判断溢出
		char sig1=num1[0];
		char sig2=num2[0];

		//接下来每四位做加法
//		while(length)
		//TODO: Unfinished
		return null;
	}
	
	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}




	//tools I write

//	should not be called directly
	private static StringBuffer cut(char[] ret){
		for(int i=0,length=ret.length;i<length-2;i++){
			if(ret[i]=='0'&&ret[i+1]=='0'){
				ret[i]=(char)0;
			}else {
				break;
			}
		}
		StringBuffer buffer=new StringBuffer();
		for(int i=0,length=ret.length;i<length;i++){
			if(ret[i]!=(char)0){
				buffer.append(ret[i]);
			}
		}
		return buffer;
	}

	//convert a decimal number to a Binary number
	private static String integerToBinary(String denumber){
		StringBuffer buffer=new StringBuffer();
		String[] tmp;
		while(!"0".equals(denumber)&&!"00".equals(denumber)){
			tmp=divid2(denumber).split(":");
			denumber=new String(cut(tmp[0].toCharArray()));
			buffer.append(tmp[1]);
		}

		//reverse
		char[] temp=new String(buffer).toCharArray();
		buffer.setLength(0);
		for(int i=temp.length-1;i>=0;i--){
			buffer.append(temp[i]);
		}
		return  new String(buffer);
	}

	//convert a decimal decimal to Binary decimal
	private static String decimal2Binary(String decimal,int length){
		char[] ret=new char[length];
		char[] temp;
		Arrays.fill(ret,(char)0);
		temp=decimal.toCharArray();
		for(int i=0;ret[length-1]==0;i++){
			temp=mult2(new String(temp)).toCharArray();
			ret[i]=temp[0];
			if(temp.length>1) {
				temp = Arrays.copyOfRange(temp, 1, temp.length);
			}else {
				temp=new char[1];
				temp[0]='0';
			}
		}
		return new String(ret);
	}

	private static String power2(String n){
		String ret="1";
		while(!"0".equals(n)&&!"00".equals(n)){
			ret=mult2(ret);
			n=sub(n,"1");
		}
		return ret;
	}

	private static String mult2(String s1){
		String s="0"+s1;
		char[] num=s.toCharArray();
		char[] carry=new char[num.length];
		Arrays.fill(carry,'0');
		char[] ret=Arrays.copyOf(carry,num.length);
		int temp;
		for(int i=num.length-1;i>0;i--){
			temp=(num[i]-'0')<<1;
			temp+=carry[i]-'0';
			if(temp>9){
				carry[i-1]='1';
				ret[i]=(char)(temp-10+'0');
			}else{
				ret[i]=(char)(temp+'0');
			}
		}
		ret[0]=(char)(carry[0]+((num[0]-'0')<<1));

//        cut 00000abcd-->0abcd
		StringBuffer buffer=cut(ret);
		return new String(buffer);
	}

	private static String sub(String s1,String s2){
		char[] num1=s1.toCharArray();
		char[] num2=s2.toCharArray();
		char[] temp1;

		//form the correct format to add

		if(num1.length>num2.length){
			int bias=num1.length-num2.length;
			temp1= Arrays.copyOf(num2,num2.length+bias+1);
			//xxxxxxx1234
			while(temp1[temp1.length-1]=='\u0000'){
				for(int i=temp1.length-1;i>0;i--) {
					temp1[i]=temp1[i-1];
				}
			}
			//00000001234
			for(int i=0;i<bias+1;i++){
				temp1[i]='0';
			}
			//01234123512
			num1=Arrays.copyOf(num1,num1.length+1);
			for(int i=num1.length-1;i>0;i--){
				num1[i]=num1[i-1];
			}

			num2=temp1;
			num1[0]='0';

		}else{
			int bias=num2.length-num1.length;
			temp1= Arrays.copyOf(num1,num1.length+bias+1);
			//xxxxxxx1234
			while(temp1[temp1.length-1]=='\u0000'){
				for(int i=temp1.length-1;i>0;i--) {
					temp1[i]=temp1[i-1];
				}
			}
			//00000001234
			for(int i=0;i<bias+1;i++){
				temp1[i]='0';
			}
			//01234123512
			num2=Arrays.copyOf(num2,num2.length+1);
			for(int i=num2.length-1;i>0;i--){
				num2[i]=num2[i-1];
			}
			num1=temp1;
			num2[0]='0';
		}

		//sub
		char temp;
		char[] carry=new char[num1.length];
		char[] ret=new char[num1.length];
		Arrays.fill(carry,(char)0);
		Arrays.fill(ret,'0');

		for(int i=num1.length-1;i>0;i--){
			temp=(char)(num1[i]-num2[i]-carry[i]+'0');
			if(temp<'0'){
				ret[i]=(char)(temp+10);
				carry[i-1]=1;
			}else{
				ret[i]=temp;
			}
		}

		//cut 00000abcd-->0abcd
		StringBuffer buffer=cut(ret);
		return new String(buffer);
	}
	private static String add(String s1,String s2){
		char[] num1=s1.toCharArray();
		char[] num2=s2.toCharArray();
		char[] temp1;
		if(num1.length>num2.length){
			int bias=num1.length-num2.length;
			temp1= Arrays.copyOf(num2,num2.length+bias+1);
			//xxxxxxx1234
			while(temp1[temp1.length-1]=='\u0000'){
				for(int i=temp1.length-1;i>0;i--) {
					temp1[i]=temp1[i-1];
				}
			}
			//00000001234
			for(int i=0;i<bias+1;i++){
				temp1[i]='0';
			}
			//01234123512
			num1=Arrays.copyOf(num1,num1.length+1);
			for(int i=num1.length-1;i>0;i--){
				num1[i]=num1[i-1];
			}
			num2=temp1;
			num1[0]='0';

		}else{
			int bias=num2.length-num1.length;
			temp1= Arrays.copyOf(num1,num1.length+bias+1);
			//xxxxxxx1234
			while(temp1[temp1.length-1]=='\u0000'){
				for(int i=temp1.length-1;i>0;i--) {
					temp1[i]=temp1[i-1];
				}
			}
			//00000001234
			for(int i=0;i<bias+1;i++){
				temp1[i]='0';
			}
			//01234123512
			num2=Arrays.copyOf(num2,num2.length+1);
			for(int i=num2.length-1;i>0;i--){
				num2[i]=num2[i-1];
			}
			num2[0]='0';
			num1=temp1;
		}
		//add
		char[] carry=new char[num1.length];
		char[] ret=new char[num1.length];
		char temp;
		Arrays.fill(carry,'0');
		Arrays.fill(ret,'0');
		for(int i=num1.length-1;i>0;i--){
			temp=(char)(carry[i]+num1[i]+num2[i]-'0'-'0');
			if(temp>'9'){
				ret[i]=(char)(temp-10);
				carry[i-1]='1';
			}else {
				ret[i]=temp;
			}
		}
		ret[0]=(char)(carry[0]+num1[0]+num2[0]-'0'-'0');

		//cut 00000abcd-->0abcd
		StringBuffer buffer=cut(ret);
		return new String(buffer);
	}

	private static String divid2(String s){
		char[] num=s.toCharArray();
		char a,b;
		int temp;
		char[] ret=new char[num.length];
		int rest=0;
		boolean flag=true;//判断个位是不是已经被计算过了（比如22／2中，个位没有被计算过，但是12／2，个位被计算了）
		Arrays.fill(ret,'0');
		for(int i=0,len=num.length;i<len-1;i++){
			a=num[i];
			b=num[i+1];
			temp=a+rest*10-0x30;
			if(temp<2){
				i++;
				a-=0x30;
				b-=0x30;
				rest=a*10+b;
				ret[i]=(char)(rest/2+0x30);
				rest%=2;
				if(i==len-1){
					flag=false;
				}
			}else {
				ret[i]=(char)((temp>>1)+'0');
				rest=temp&1;
			}
		}
		if(flag) {
			temp = num[num.length - 1] + rest * 10 - 0x30;
			ret[num.length - 1] = (char) ((temp >> 1) + '0');
			rest=temp&1;
		}

		StringBuffer buffer=new StringBuffer(new String(ret));
		buffer.append(":");
		buffer.append(rest);




		return new String(buffer);
	}
}
