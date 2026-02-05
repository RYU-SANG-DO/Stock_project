package egovframework.stock.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Font;

public class ExcelUtil {
	
	private static final int CELL_TYPE_NUMERIC = 0;
	private static final int CELL_TYPE_STRING = 1;
	private static final int CELL_TYPE_FORMULA = 2;
	private static final int CELL_TYPE_BLANK = 3;
	private static final int CELL_TYPE_BOOLEAN = 4;
	private static final int CELL_TYPE_ERROR = 5;

	/**
	 * 엑셀파일 내용 반환
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getExcelDataList(String saveFilePath) throws Exception{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if( saveFilePath.equals("") ){
			return null;
		}

		Workbook workbook = Workbook.getWorkbook(new File(saveFilePath));
		Sheet sheet  = workbook.getSheet(0);

		int row = sheet.getRows();
		Map<String, Object> inputMap;
		String oldKey = "";
		int merge = 0;
		for( int i=0; i<row; i++ ){
			inputMap = new HashMap<String, Object>();
			Cell cell[] = sheet.getRow(i);
			for( int j=0; j<cell.length; j++ ){
				inputMap.put("parameter"+j, cell[j].getContents());

				//System.out.println(i + "-" + j +")" + cell[j].getContents());
			}
			if( !oldKey.equals(cell[0].getContents()) ){
				merge = 0;
				oldKey = cell[0].getContents();
				for( int idx=0; idx<row; idx++ ){
					Cell cell2[] = sheet.getRow(idx);
					if( cell2[0].getContents().equals(oldKey) ){
						merge++;
					}
				}
			}
			inputMap.put("merge", merge);
			list.add(inputMap);
		}

		return list;
	}
	
	/**
	 * @Method : mnspXlsxExcelReader_V01
	 * @Description : 약수터 검사결과 엑셀 파일 읽기(먹는물 공동시설 검사결과)
	 * 						  xlsx 파일 형식 vo 파싱 2021년검사 항목 변경
	 * @param req : multipartHttpServletRequest
	 * @param sheetNum : 데이터 있는 시트 넘버 (0부터 시작 (ex. 첫번째 시트를 불러올때 0 입력))
	 * @param rowNum : 실 데이터 시작 로우 (0부터 시작 (ex. 3번째 줄부터 실제 데이터가 시작되면 2입력))
	 * @param cellNum : 실 데이터 시작 컬럼 (0부터 시작 (ex. 3번째 컬럼부터 실제 데이터가 시작되면 2입력))
	 * @return
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static List<Map<String, Object>> xlsxExcelReader(String saveFilePath, int sheetNum, int rowNum, int cellNum) {
		//System.out.println("mnspXlsxExcelReader_V01 start");
		//System.out.println(saveFilePath);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		XSSFWorkbook workbook = null;

		try {
			// HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
			//workbook = new XSSFWorkbook(new File(saveFilePath));
			FileInputStream fis = new FileInputStream(saveFilePath);

			workbook = new XSSFWorkbook(fis);

			// 탐색에 사용할 Sheet, Row, Cell 객체
			XSSFSheet curSheet;
			XSSFRow curRow;
			XSSFCell curCell;
			Map<String, Object> map = new HashMap<String,Object>();

			// 선택 sheet 반환
			curSheet = workbook.getSheetAt(sheetNum);
			int rows = curSheet.getPhysicalNumberOfRows();
			// row 탐색 for문
			for (int rowIndex = rowNum; rowIndex < rows; rowIndex++) {
				// row 0은 헤더정보이기 때문에 무시
				if (rowIndex != 0) {
					curRow = curSheet.getRow(rowIndex);
					String value;
					// row의 첫번째 cell값이 비어있지 않는 경우만 cell탐색
					if (curRow.getCell(cellNum) != null) {
						//if (!"".equals(curRow.getCell(cellNum).getStringCellValue())) {
						if (!"".equals(curRow.getCell(cellNum))) {
							// cell 탐색 for문
							int cells = curRow.getPhysicalNumberOfCells();
							//System.out.print(rowIndex+".");
							map = new HashMap<String,Object>();
							for (int cellIndex = cellNum; cellIndex < curRow.getLastCellNum(); cellIndex++) {
								curCell = curRow.getCell(cellIndex);
								
								if(curCell == null) {
									curCell = curRow.createCell(cellIndex);
									curCell.setCellValue("");
								}
								if (true) {
//									if (curCell != null) {
									value = "";
									// cell 스타일이 다르더라도 String으로 반환 받음
									int type = curCell.getCellType();
									
									switch (curCell.getCellType()) {
									case  CELL_TYPE_FORMULA:
										// 함수 타입에 따른 분류 - 수식값만 가져옴
										switch (curCell.getCachedFormulaResultType()) {
										case CELL_TYPE_STRING:
											value = curCell.getStringCellValue() + "";
											break;
										case CELL_TYPE_NUMERIC:
											Double numericCellValue = curCell.getNumericCellValue();

											if (Math.floor(numericCellValue) == numericCellValue) { // 소수점 이하를 버린 값이 원래의
																									// 값과 같다면,,

												value = numericCellValue.intValue() + ""; // 정수

											} else {
												// 소수점 3째자리로 표출

												value = Math.round(numericCellValue * 1000) / 1000.0 + "";

											}

											break;
										case CELL_TYPE_BOOLEAN:

											break;
										case CELL_TYPE_ERROR:

											break;
										}

										break;
									case CELL_TYPE_NUMERIC:// 날짜 형식이든 숫자 형식이든 다 CELL_TYPE_NUMERIC으로 인식함.

										if (DateUtil.isCellDateFormatted(curCell)) { // 날짜 유형의 데이터일 경우,

											SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

											String formattedStr = dateFormat.format(curCell.getDateCellValue());

											value = formattedStr;

											break;

										} else { // 순수하게 숫자 데이터일 경우,

											Double numericCellValue = curCell.getNumericCellValue();

											if (Math.floor(numericCellValue) == numericCellValue) { // 소수점 이하를 버린 값이 원래의
																									// 값과 같다면,,

												value = numericCellValue.intValue() + ""; // 정수

											} else {
												// 소수점 3째자리로 표출
												value = Math.round(numericCellValue * 1000) / 1000.0 + "";

											}

											break;

										}
									case CELL_TYPE_STRING:
										value = curCell.getStringCellValue() + "";
										break;
									case CELL_TYPE_BLANK:
										value = "";
										break;
									case CELL_TYPE_ERROR:
										value = curCell.getErrorCellValue() + "";
										break;
									case CELL_TYPE_BOOLEAN:
										value = curCell.getBooleanCellValue() + "";
										break;
									default:
										value = new String();
										break;
									} // end switch
									// 현재 colum index에 따라서 vo입력
									//setNspExcelReaderSwith(cellIndex, value, vo);
								} // end if
								map.put("parameter"+(cellIndex), value);
								//System.out.print(value);
							} // end for
							//System.out.println(map);
							list.add(map);
						} // end
					} else {
						//return list;
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("mnspXlsxExcelReader_V01 end");
		return list;
		}
	
	/**
	 * @Method : xlsExcelReader
	 * @Description : xls 형태 엑셀 vo 파싱
	 * @param req : multipartHttpServletRequest
	 * @param sheetNum : 데이터 있는 시트 넘버 (0부터 시작 (ex. 첫번째 시트를 불러올때 0 입력))
	 * @param rowNum : 실 데이터 시작 로우 (0부터 시작 (ex. 3번째 줄부터 실제 데이터가 시작되면 2입력))
	 * @param cellNum : 실 데이터 시작 컬럼 (0부터 시작 (ex. 3번째 컬럼부터 실제 데이터가 시작되면 2입력))
	 * @return
	 */
	//public List<ExcelVO> mnspXlsExcelReader(MultipartHttpServletRequest req, int sheetNum, int rowNum, int cellNum) {
	public List<Map<String, Object>> xlsExcelReader(String saveFilePath, int sheetNum, int rowNum, int cellNum) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 반환할 객체를 생성
		//List<ExcelVO> list = new ArrayList<ExcelVO>();

		//MultipartFile file = req.getFile("file");
		HSSFWorkbook workbook = null;
		try {
			FileInputStream fis = new FileInputStream(saveFilePath);
			// HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
			workbook = new HSSFWorkbook(fis);

			// 탐색에 사용할 Sheet, Row, Cell 객체
			HSSFSheet curSheet;
			HSSFRow curRow;
			HSSFCell curCell;
			Map<String, Object> map = new HashMap<String,Object>();

			// 현재 sheet 반환
			curSheet = workbook.getSheetAt(sheetNum);
			// row 탐색 for문
			for (int rowIndex = rowNum; rowIndex < curSheet.getPhysicalNumberOfRows(); rowIndex++) {
				// row 0은 헤더정보이기 때문에 무시
				if (rowIndex != 0) {
					curRow = curSheet.getRow(rowIndex);
					String value;
					// cell 탐색 for문
					for (int cellIndex = cellNum; cellIndex < curRow.getLastCellNum(); cellIndex++) {
						curCell = curRow.getCell(cellIndex);
							if (curCell != null) {
								value = "";
								// cell 스타일이 다르더라도 String으로 반환 받음
								//CellType type = curCell.getCellType();								
								switch (curCell.getCellType()) {
								case CELL_TYPE_FORMULA:
									value = curCell.getCellFormula();
									break;
								case CELL_TYPE_NUMERIC:
									// value = curCell.getNumericCellValue() + "";
									if (DateUtil.isCellDateFormatted(curCell)) {
										Date date = curCell.getDateCellValue();
										value = new SimpleDateFormat("yyyy-MM-dd").format(date);
									} else {
										curCell.setCellType(CELL_TYPE_STRING);
										value = curCell.getStringCellValue();
									}
									break;
								case CELL_TYPE_STRING:
									value = curCell.getStringCellValue() + "";
									break;
								case CELL_TYPE_BLANK:
									value = "";
									break;
								case CELL_TYPE_ERROR:
									value = curCell.getErrorCellValue() + "";
									break;
								case CELL_TYPE_BOOLEAN:
									value = curCell.getBooleanCellValue() + "";
									break;
								default:
									value = new String();
									break;
								} // end switch

								//setNspExcelReaderSwith(cellIndex, value, vo);
								map.put("parameter"+cellIndex, value);
							} // end if
						} // end for
							// cell 탐색 이후 vo 추가
						list.add(map);
					} // end


			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 엑셀 파일 생성(종목 상세 정보) 
	 * 코스피 종목 상세 정보
	 * v00
	 * @param mnspList
	 * @param reqMap
	 * @param sheetNum
	 * @param rowNum
	 * @return
	 */
	public static Map<String,Object> resultSetMnspXlsxExcelCreateV00(List<Map<String, Object>> resultList , Map<String,Object> reqMap, int sheetNum, int rowNum) {
		//System.out.println("resultSetMnspXlsxExcelCreate start");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		FileOutputStream outStream = null;
		InputStream fis = null;	
		String filePath = StringUtil.nvl(reqMap.get("filePath"),"");
		String fileName = StringUtil.nvl(reqMap.get("fileName"),"cospi_form.xlsx");
		String fileResultName = StringUtil.nvl(reqMap.get("fileResultName"),"cospi_result.xlsx");
		try {
			String year = StringUtil.nvl(reqMap.get("searchYear"),ComDateUtil.getYear());
			String quarter = StringUtil.nvl(reqMap.get("searchQuarter"),ComDateUtil.getQuarter());
			fis = new FileInputStream(filePath+ "/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fis); // xlsx 파일 Open
			
	        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
	        XSSFRow curRow;
	        XSSFCell curCell;
	        for (int rowIndex = rowNum; rowIndex < resultList.size()+rowNum; rowIndex++) {
	        	Map<String, Object> map = resultList.get(rowIndex);
	        	String name0 = StringUtil.nvl(map.get("parameter0"),"");
        		String useYn = StringUtil.nvl(map.get("useYn"),"Y");
	        	//Map<String, Object> voMap = BeanUtils.describe(vo);
	        	curRow = sheet.createRow(rowIndex+1);    // row 생성
	        	XSSFCellStyle cellStyle2 = workbook.createCellStyle();
	        	cellStyle2.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle2.setBorderRight(BorderStyle.THIN);
	    		cellStyle2.setBorderLeft(BorderStyle.THIN);
	    		cellStyle2.setBorderTop(BorderStyle.THIN);
	    		cellStyle2.setBorderBottom(BorderStyle.THIN);
	    		cellStyle2.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 노란색
	    		cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFCellStyle cellStyle1 = workbook.createCellStyle();
	        	//CellStyle cellStyle = workbook.createCellStyle();
	    		cellStyle1.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle1.setBorderRight(BorderStyle.THIN);
	    		cellStyle1.setBorderLeft(BorderStyle.THIN);
	    		cellStyle1.setBorderTop(BorderStyle.THIN);
	    		cellStyle1.setBorderBottom(BorderStyle.THIN);
	        	cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFFont RedBold = workbook.createFont();
	        	RedBold.setColor(HSSFColor.RED.index);
	        	RedBold.setFontName("나눔고딕"); //글씨체
	        	//RedBold3.setFontHeight((short)(14*20)); //사이즈
	        	//RedBold3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); //볼드 (굵게)
	        	XSSFFont BludBold = workbook.createFont();
	        	BludBold.setFontName("나눔고딕"); //글씨체
	        	BludBold.setColor(HSSFColor.BLUE.index);
	        	
	        	XSSFFont GreyBold = workbook.createFont();
	        	GreyBold.setFontName("나눔고딕"); //글씨체
	        	GreyBold.setColor(HSSFColor.GREY_50_PERCENT.index);
	        	//GreyBold.setBold(false);

	        	for (int cellIndex = 0; cellIndex <  map.entrySet().size(); cellIndex++) {
	        		XSSFCellStyle cellStyle3 = workbook.createCellStyle();
	        		cellStyle3.setBorderRight(BorderStyle.THIN);
	        		cellStyle3.setBorderLeft(BorderStyle.THIN);
	        		cellStyle3.setBorderTop(BorderStyle.THIN);
	        		cellStyle3.setBorderBottom(BorderStyle.THIN);
	        		cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
	        		
	        		curCell = curRow.createCell(cellIndex);
	        		String pam = StringUtil.nvl(map.get("parameter"+cellIndex),"");
	        		
	        		String pam16 = StringUtil.nvl(map.get("parameter16"),"");
	        		String pam1 = StringUtil.nvl(map.get("parameter1"),"");
	        		
	        		if(StringUtil.nvl(reqMap.get("codes"),"").indexOf(pam1) > (-1)) {
	        			cellStyle3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        			cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        			BludBold.setBold(true);
	        			RedBold.setBold(true);
	        		}else {
	        			BludBold.setBold(false);
	        			RedBold.setBold(false);
	        		
	        		}
	        		if("N".equals(useYn)) {
	        			cellStyle3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        			cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        			GreyBold.setBold(true);
	        			cellStyle3.setFont(GreyBold);
        			}else {
        				if("하락".equals(pam16)) {
        					cellStyle3.setFont(BludBold);
        				}else if("상승".equals(pam16)) {
        					cellStyle3.setFont(RedBold);
        				}
        			}
	        		
	        		
	        		if(cellIndex == 16) {//상태
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        		}else if(cellIndex == 1) {//종목코드
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	        		}else if(cellIndex == 4) {//상장일
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        		}else if(cellIndex == 5) {//결산월
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        		}else if(cellIndex == 6) {//대표자명
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        		}else if(cellIndex == 8) {//지역
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        		}else if(cellIndex >= 10 && cellIndex <= 15) {//
	        			cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
	        		}else if(cellIndex == 17) {//전일대비
	        			cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
	        		}else if(cellIndex == 18) {//가져오는 시간
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        		}
	        		curCell.setCellStyle(cellStyle3);
	        		curCell.setCellValue(StringUtil.nvl(map.get("parameter"+cellIndex),""));
	        	}
	        }
			outStream = new FileOutputStream(filePath+ "/" + fileResultName);
			workbook.write(outStream);
			resultMap.put("code", "00");
			resultMap.put("codeNm", "정상");
		} catch (Exception e){
			resultMap.put("code", "21");
			resultMap.put("codeNm", "파일생성 오류");
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("resultSetMnspXlsxExcelCreate end");
		return resultMap;
	}
	
	/**
	 * 엑셀 파일 생성(종목 상세 정보) 
	 * 코스피 종목 상세 정보
	 * v01
	 * @param mnspList
	 * @param reqMap
	 * @param sheetNum
	 * @param rowNum
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Object> resultSetMnspXlsxExcelCreateV01(
				List<Map<String, Object>> resultList , Map<String,Object> reqMap, 
				String version, 
				int sheetNum, 
				int rowNum,
				LinkedList<String> stockTitles,
				LinkedList<Boolean> stockUserYn
			) throws Exception {
		//System.out.println("resultSetMnspXlsxExcelCreate start");
		System.out.println("엑셀다운로드 시작");
		LinkedList<Integer> stockIndex = new LinkedList<Integer>();
		System.out.println(stockTitles);
		System.out.println(stockUserYn);
		
		version = StringUtil.nvl(version,"_v01");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		FileOutputStream outStream = null;
		InputStream fis = null;	
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmmss");
		String filePath = StringUtil.nvl(reqMap.get("filePath"),"");
		String fileName = StringUtil.nvl(reqMap.get("fileName"),"cospi_form_v02.xlsx");
		//String fileResultName = StringUtil.nvl(reqMap.get("fileResultName"),"cospi_result"+version+".xlsx");
		String fileResultName = StringUtil.nvl(reqMap.get("fileResultName"),"코스피"+version+"_"+today+".xlsx");
		try {
			String year = StringUtil.nvl(reqMap.get("searchYear"),ComDateUtil.getYear());
			String quarter = StringUtil.nvl(reqMap.get("searchQuarter"),ComDateUtil.getQuarter());
			fis = new FileInputStream(filePath+ "/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fis); // xlsx 파일 Open
			
	        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
	        XSSFRow curRow;
	        XSSFCell curCell;
	        curRow = sheet.createRow(0);    // row 생성
	        XSSFCellStyle cellStyle5 = workbook.createCellStyle();
	        cellStyle5.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
    		//테두리 선 (우,좌,위,아래)
	        cellStyle5.setBorderRight(BorderStyle.THIN);
	        cellStyle5.setBorderLeft(BorderStyle.THIN);
	        cellStyle5.setBorderTop(BorderStyle.THIN);
	        cellStyle5.setBorderBottom(BorderStyle.THIN);
	        cellStyle5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        cellStyle5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        int idx = 0;
	        int idx9 = 0;
	        for(int i = 0 ; i < 10 ; i++) {
	        	if(stockUserYn.get(i)) {
	        		idx9=i;
	        	}
	        }
	       // System.out.println(idx9);
	        int sidx = 0;
//	        for(int i = 0 ; i < stockUserYn.size() ; i++) {
//	        	if(stockUserYn.get(i)) {
//	        		if(idx9 != i) {
//		        		System.out.print(i+",");
//		        		stockIndex.add(i);
//		        		sidx = i;
//	        		}
//	        		curCell = curRow.createCell(idx++);
//	        		curCell.setCellStyle(cellStyle5);
//	        		curCell.setCellValue(StringUtil.nvl(stockTitles.get(i),"")+((idx-1)+"_"+sidx));
//	        	}
//	        }
	        for(int i = 0 ; i < stockUserYn.size() ; i++) {
	        	if(stockUserYn.get(i)) {
	        		stockIndex.add(i);
	        		System.out.print(i+",");	        		
	        		curCell = curRow.createCell(idx++);
	        		curCell.setCellStyle(cellStyle5);
	        		//curCell.setCellValue(StringUtil.nvl(stockTitles.get(i),"")+((idx-1)+"_"+i));
	        		curCell.setCellValue(StringUtil.nvl(stockTitles.get(i),""));
	        	}
	        }
	        System.out.println();
	        //System.out.println(stockIndex);
	        for (int rowIndex = rowNum; rowIndex < resultList.size()+rowNum; rowIndex++) {
	        	Map<String, Object> map = resultList.get(rowIndex);
	        	String name0 = StringUtil.nvl(map.get("parameter0"),"");
        		String useYn = StringUtil.nvl(map.get("useYn"),"Y");
	        	//Map<String, Object> voMap = BeanUtils.describe(vo);
	        	curRow = sheet.createRow(rowIndex+1);    // row 생성
	        	XSSFCellStyle cellStyle2 = workbook.createCellStyle();
	        	cellStyle2.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle2.setBorderRight(BorderStyle.THIN);
	    		cellStyle2.setBorderLeft(BorderStyle.THIN);
	    		cellStyle2.setBorderTop(BorderStyle.THIN);
	    		cellStyle2.setBorderBottom(BorderStyle.THIN);
	    		cellStyle2.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 노란색
	    		cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFCellStyle cellStyle1 = workbook.createCellStyle();
	        	//CellStyle cellStyle = workbook.createCellStyle();
	    		cellStyle1.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle1.setBorderRight(BorderStyle.THIN);
	    		cellStyle1.setBorderLeft(BorderStyle.THIN);
	    		cellStyle1.setBorderTop(BorderStyle.THIN);
	    		cellStyle1.setBorderBottom(BorderStyle.THIN);
	        	cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFFont RedBold = workbook.createFont();
	        	RedBold.setColor(HSSFColor.RED.index);
	        	RedBold.setFontName("나눔고딕"); //글씨체
	        	//RedBold3.setFontHeight((short)(14*20)); //사이즈
	        	//RedBold3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); //볼드 (굵게)
	        	XSSFFont BludBold = workbook.createFont();
	        	BludBold.setFontName("나눔고딕"); //글씨체
	        	BludBold.setColor(HSSFColor.BLUE.index);
	        	
	        	XSSFFont GreyBold = workbook.createFont();
	        	GreyBold.setFontName("나눔고딕"); //글씨체
	        	GreyBold.setColor(HSSFColor.GREY_50_PERCENT.index);
	        	//GreyBold.setBold(false);
	        	
//	        	curCell = curRow.createCell(0);
//	        	XSSFCellStyle cellStyle0 = workbook.createCellStyle();
//	        	cellStyle0.setBorderRight(BorderStyle.THIN);
//        		cellStyle0.setBorderLeft(BorderStyle.THIN);
//        		cellStyle0.setBorderTop(BorderStyle.THIN);
//        		cellStyle0.setBorderBottom(BorderStyle.THIN);
//        		cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER);
//        		cellStyle0.setAlignment(HorizontalAlignment.CENTER);
//	        	curCell.setCellStyle(cellStyle0);
//	        	curCell.setCellValue(rowIndex);
	        	//for (int cellIndex = 0; cellIndex <  map.entrySet().size(); cellIndex++) {
	        	for (int cellIndex = 0; cellIndex <  	stockIndex.size(); cellIndex++) {
	        		int indxValue = stockIndex.get(cellIndex);
	        		//System.out.print(indxValue+":");
	        		XSSFCellStyle cellStyle3 = workbook.createCellStyle();
	        		cellStyle3.setBorderRight(BorderStyle.THIN);
	        		cellStyle3.setBorderLeft(BorderStyle.THIN);
	        		cellStyle3.setBorderTop(BorderStyle.THIN);
	        		cellStyle3.setBorderBottom(BorderStyle.THIN);
	        		cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
	        		
	        		curCell = curRow.createCell(cellIndex);
	        		String pam = StringUtil.nvl(map.get("parameter"+indxValue),"");
	        		String pam16 = StringUtil.nvl(map.get("parameter17"),"");
	        		if("_v03".equals(version))pam16 = StringUtil.nvl(map.get("parameter13"),"");
	        		String pam1 = StringUtil.nvl(map.get("parameter1"),"");
	        		
	        		if(StringUtil.nvl(reqMap.get("codes"),"").indexOf(pam1) > (-1)) {
	        			cellStyle3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        			cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        			BludBold.setBold(true);
	        			RedBold.setBold(true);
	        		}else {
	        			BludBold.setBold(false);
	        			RedBold.setBold(false);
	        		
	        		}
	        		if("N".equals(useYn)) {
	        			cellStyle3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        			cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        			GreyBold.setBold(true);
	        			cellStyle3.setFont(GreyBold);
        			}else {
        				if("하락".equals(pam16)) {
        					cellStyle3.setFont(BludBold);
        				}else if("상승".equals(pam16)) {
        					cellStyle3.setFont(RedBold);
        				}
        			}
	        		
	        		if("_v03".equals(version)) {
	        			if(indxValue == 0 || indxValue == 1 || indxValue == 2 || indxValue == 13 || indxValue == 15) {//종목코드,상태,가져오는 시간
	        				cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        			}else if(indxValue >= 3 && indxValue <= 12 || indxValue == 14 || indxValue == 17 || indxValue == 18) {//
	        				cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
	        			}else if(indxValue >= 20) {
	        				cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
	        			}
	        		}else {
	        			if(indxValue == 0 || indxValue == 1 || indxValue == 2 || indxValue == 17 || indxValue == 19) {//종목코드,상태,가져오는 시간
	        				cellStyle3.setAlignment(HorizontalAlignment.CENTER);
	        			}else if(indxValue >= 11 && indxValue <= 16 || indxValue == 18) {//
	        				cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
	        			}else if(indxValue > 20) {
	        				cellStyle3.setAlignment(HorizontalAlignment.RIGHT);
	        			}
	        		}
	        		curCell.setCellStyle(cellStyle3);
	        		curCell.setCellValue(StringUtil.nvl(map.get("parameter"+(indxValue)),""));
	        	}
	        	//System.out.println();
	        }
			outStream = new FileOutputStream(filePath+ "/" + fileResultName);
			workbook.write(outStream);
			resultMap.put("code", "00");
			resultMap.put("codeNm", "정상");
		} catch (Exception e){
			resultMap.put("code", "21");
			resultMap.put("codeNm", "파일생성 오류");
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("엑셀다운로드 종료");
		}
		//System.out.println("resultSetMnspXlsxExcelCreate end");
		return resultMap;
	}
	
	/**
	 * 엑셀 파일 생성
	 * @param mnspList
	 * @param reqMap
	 * @param sheetNum
	 * @param rowNum
	 * @return
	 */
	public static Map<String,Object> resultSetMnspXlsxExcelCreateV02(List<Map<String, Object>> resultList , Map<String,Object> reqMap, int sheetNum, int rowNum) {
		System.out.println("resultSetMnspXlsxExcelCreateV02("+ resultList.size()+") start");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		FileOutputStream outStream = null;
		InputStream fis = null;	
		String p_type = StringUtil.nvl(reqMap.get("p_type"),"");
		System.out.println("p_type=>"+p_type);
		String resultFilePath = StringUtil.nvl(reqMap.get("resultFilePath"),"");
		String filePath = StringUtil.nvl(reqMap.get("filePath"),"");
		String fileName = StringUtil.nvl(reqMap.get("fileName"),"stock_form.xlsx");
		String fileResultName = StringUtil.nvl(reqMap.get("fileResultName"),"stock_result.xlsx");
		try {
			String year = StringUtil.nvl(reqMap.get("searchYear"),ComDateUtil.getYear());
			String quarter = StringUtil.nvl(reqMap.get("searchQuarter"),ComDateUtil.getQuarter());
			System.out.println(filePath+ "/" + fileName);
			fis = new FileInputStream(filePath+ "/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fis); // xlsx 파일 Open
			
	        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
	        XSSFRow curRow;
	        XSSFCell curCell;
	        for (int rowIndex = rowNum; rowIndex < resultList.size()+rowNum; rowIndex++) {
	        	Map<String, Object> map = resultList.get(rowIndex);
	        	System.out.println(map);
	        	//Map<String, Object> voMap = BeanUtils.describe(vo);
	        	curRow = sheet.createRow(rowIndex+1);    // row 생성
	        	XSSFCellStyle cellStyle2 = workbook.createCellStyle();
	        	cellStyle2.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle2.setBorderRight(BorderStyle.THIN);
	    		cellStyle2.setBorderLeft(BorderStyle.THIN);
	    		cellStyle2.setBorderTop(BorderStyle.THIN);
	    		cellStyle2.setBorderBottom(BorderStyle.THIN);
	    		cellStyle2.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 노란색
	    		cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFCellStyle cellStyle1 = workbook.createCellStyle();
	        	//CellStyle cellStyle = workbook.createCellStyle();
	    		cellStyle1.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle1.setBorderRight(BorderStyle.THIN);
	    		cellStyle1.setBorderLeft(BorderStyle.THIN);
	    		cellStyle1.setBorderTop(BorderStyle.THIN);
	    		cellStyle1.setBorderBottom(BorderStyle.THIN);
	        	cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFFont RedBold = workbook.createFont();
	        	RedBold.setColor(HSSFColor.RED.index);
	        	RedBold.setFontName("나눔고딕"); //글씨체
	        	//RedBold3.setFontHeight((short)(14*20)); //사이즈
	        	//RedBold3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); //볼드 (굵게)
	        	
	        	XSSFFont BludBold = workbook.createFont();
	        	BludBold.setColor(HSSFColor.BLUE.index);
	        	BludBold.setFontName("나눔고딕"); //글씨체
	        	
	        	String pam8 = StringUtil.nvl(map.get("parameter8"),"");//
	        	
	        	for (int cellIndex = 0; cellIndex <  map.entrySet().size(); cellIndex++) {
	        		curCell = curRow.createCell(cellIndex);
	        		String pam = StringUtil.nvl(map.get("parameter"+cellIndex),"").trim();
	        		String pam2 = StringUtil.nvl(map.get("parameter2"),"").trim();
	        		System.out.println(cellIndex+":"+pam+":"+pam2);
//	        		if(!"THEME".equals(p_type)){
//		        		 if(cellIndex == 4) {
//		        			 if("0".equals(pam2)){
//		        				 pam = "";
//		        			 }else {
//		        				 if("*".equals(pam)) {
//		        					 pam = "코스닥";
//		        				 }else {
//		        					 pam = "코스피";
//		        				 }
//		        			 }
//			        	}
//	        		}
	        		if(!"THEME".equals(p_type)){
		        		if((cellIndex == 3 || cellIndex == 6 || cellIndex == 7 ) && !"0".equals(pam2)) {
		        			if(pam.indexOf("+") > (-1)) {
		        				XSSFCellStyle cs = workbook.createCellStyle();
		        				cs.setAlignment(HorizontalAlignment. RIGHT);
		        				cs.setFont(RedBold);
		        				curCell.setCellStyle(cs);
		        			}else if(pam.indexOf("-") > (-1)) {
		        				XSSFCellStyle cs = workbook.createCellStyle();
		        				cs.setAlignment(HorizontalAlignment. RIGHT);
		        				cs.setFont(BludBold);
		        				curCell.setCellStyle(cs);
		        			}else {
		        				XSSFCellStyle cs = workbook.createCellStyle();
		        				cs.setAlignment(HorizontalAlignment. RIGHT);
		        				curCell.setCellStyle(cs);
		        			}
	//	        			pam = pam.replace("+", "").replace("%", "");
		        		}else if(cellIndex < 6) {
		        			XSSFCellStyle cs = workbook.createCellStyle();
	        				cs.setAlignment(HorizontalAlignment. CENTER);
	        				curCell.setCellStyle(cs);
		        		}
	        		}else {
	        			if(cellIndex < 3 || cellIndex == 5 || cellIndex == 7) {
	        				XSSFCellStyle cs = workbook.createCellStyle();
	        				cs.setAlignment(HorizontalAlignment. CENTER);
	        				curCell.setCellStyle(cs);
	        			}else if(cellIndex == 3) {
	        				if(pam.indexOf("+") > (-1)) {
		        				XSSFCellStyle cs = workbook.createCellStyle();
		        				cs.setAlignment(HorizontalAlignment. RIGHT);
		        				cs.setFont(RedBold);
		        				curCell.setCellStyle(cs);
		        			}else if(pam.indexOf("-") > (-1)) {
		        				XSSFCellStyle cs = workbook.createCellStyle();
		        				cs.setAlignment(HorizontalAlignment. RIGHT);
		        				cs.setFont(BludBold);
		        				curCell.setCellStyle(cs);
		        			}
	        			}
	        		}
	        		
	        		curCell.setCellValue(pam);
//	        	    cs.setWrapText(true);

	        	}
	        }
			outStream = new FileOutputStream(resultFilePath+ "/" + fileResultName);
			workbook.write(outStream);
			resultMap.put("code", "00");
			resultMap.put("codeNm", "정상");
		} catch (Exception e){
			resultMap.put("code", "21");
			resultMap.put("codeNm", "파일생성 오류");
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("resultSetMnspXlsxExcelCreateV02 end");
		return resultMap;
	}
	
	
	/**
	 * 엑셀 파일 생성
	 * @param mnspList
	 * @param reqMap
	 * @param sheetNum
	 * @param rowNum
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Object> resultSetMnspXlsxExcelCreateV03(List<Map<String, Object>> resultList , String [] stockTitles, int sheetNum, int rowNum) throws Exception {
		//System.out.println("resultSetMnspXlsxExcelCreate start");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		FileOutputStream outStream = null;
		InputStream fis = null;	
		//String filePath = StringUtil.nvl(reqMap.get("filePath"),"");
		String year = ComDateUtil.getYear();
		String mm = ComDateUtil.getMonth_mm();
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmmss");
		String filePath = "D:/eclipse_java/com/stock";
		String fileName = "stock_form.xlsx";
		String fileResultName = "코스피_paxnet_"+today+".xlsx";
		try {
			
			fis = new FileInputStream(filePath+ "/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fis); // xlsx 파일 Open
			
	        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
	        XSSFRow curRow;
	        XSSFCell curCell;
	        curRow = sheet.createRow(0);    // row 생성
	        XSSFCellStyle cellStyle5 = workbook.createCellStyle();
	        cellStyle5.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
    		//테두리 선 (우,좌,위,아래)
	        cellStyle5.setBorderRight(BorderStyle.THIN);
	        cellStyle5.setBorderLeft(BorderStyle.THIN);
	        cellStyle5.setBorderTop(BorderStyle.THIN);
	        cellStyle5.setBorderBottom(BorderStyle.THIN);
	        cellStyle5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        cellStyle5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        for(int i = 0 ; i < stockTitles.length ; i++) {
	        		curCell = curRow.createCell(i);
	        		curCell.setCellStyle(cellStyle5);
	        		curCell.setCellValue(stockTitles[i]);
	        }
	        
	        for (int rowIndex = rowNum; rowIndex < resultList.size()+rowNum; rowIndex++) {
	        	Map<String, Object> map = resultList.get(rowIndex);
	        	//System.out.println(rowIndex+":"+map);
	        	//Map<String, Object> voMap = BeanUtils.describe(vo);
	        	curRow = sheet.createRow(rowIndex+1);    // row 생성
	        	XSSFCellStyle cellStyle2 = workbook.createCellStyle();
	        	cellStyle2.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle2.setBorderRight(BorderStyle.THIN);
	    		cellStyle2.setBorderLeft(BorderStyle.THIN);
	    		cellStyle2.setBorderTop(BorderStyle.THIN);
	    		cellStyle2.setBorderBottom(BorderStyle.THIN);
	    		cellStyle2.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 노란색
	    		cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFCellStyle cellStyle1 = workbook.createCellStyle();
	        	//CellStyle cellStyle = workbook.createCellStyle();
	    		cellStyle1.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle1.setBorderRight(BorderStyle.THIN);
	    		cellStyle1.setBorderLeft(BorderStyle.THIN);
	    		cellStyle1.setBorderTop(BorderStyle.THIN);
	    		cellStyle1.setBorderBottom(BorderStyle.THIN);
	        	cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFFont RedBold = workbook.createFont();
	        	RedBold.setColor(HSSFColor.RED.index);
	        	RedBold.setFontName("나눔고딕"); //글씨체
	        	//RedBold3.setFontHeight((short)(14*20)); //사이즈
	        	//RedBold3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); //볼드 (굵게)
	        	XSSFFont BludBold = workbook.createFont();
	        	BludBold.setFontName("나눔고딕"); //글씨체
	        	BludBold.setColor(HSSFColor.BLUE.index);
	        	XSSFFont BlackBold = workbook.createFont();
	        	BlackBold.setFontName("나눔고딕"); //글씨체
	        	BlackBold.setColor(HSSFColor.BLACK.index);
	        		        	

	        	for (int cellIndex = 0; cellIndex <  map.entrySet().size(); cellIndex++) {
	        		XSSFCellStyle cellStyle3 = workbook.createCellStyle();
	        		cellStyle3.setBorderRight(BorderStyle.THIN);
	        		cellStyle3.setBorderLeft(BorderStyle.THIN);
	        		cellStyle3.setBorderTop(BorderStyle.THIN);
	        		cellStyle3.setBorderBottom(BorderStyle.THIN);
	        		cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
	        		cellStyle3.setAlignment(HorizontalAlignment.RIGHT); //가운데 정렬
	        		String pam = StringUtil.nvl(map.get("parameter"+cellIndex),"");
	        		String pam3 = StringUtil.nvl(map.get("parameter3"),"");
	        		//System.out.print("["+pam+"]");
	        		if(cellIndex == 3) {
	        			if(pam.indexOf("상향")> (-1)) {
	    					cellStyle3.setFont(RedBold);
	    				}else if(pam.indexOf("하한")> (-1)) {
	    					cellStyle3.setFont(BludBold);
	    				}
	        		}else if(cellIndex > 3) {
	        			if(pam.indexOf("+")> (-1)) {
	    					cellStyle3.setFont(RedBold);
	    				}else if(pam.indexOf("-")> (-1)) {
	    					cellStyle3.setFont(BludBold);
	    				}
	        		}else if(cellIndex == 2) {
	        			if(pam3.indexOf("상향")> (-1)) {
	    					cellStyle3.setFont(RedBold);
	    				}else if(pam3.indexOf("하한")> (-1)) {
	    					cellStyle3.setFont(BludBold);
	    				}
	        		}else {
	        			if(pam3.indexOf("상향")> (-1)) {
	    					cellStyle3.setFont(RedBold);
	    				}else if(pam3.indexOf("하한")> (-1)) {
	    					cellStyle3.setFont(BludBold);
	    				}
	        			cellStyle3.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	        		}
//	        		 if(pam.indexOf("+")> (-1)) {
//    					cellStyle3.setFont(RedBold);
//    				}else if(pam.indexOf("-")> (-1)) {
//    					cellStyle3.setFont(BludBold);
//    				}else {
//    					cellStyle3.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
//    					cellStyle3.setFont(BlackBold);
//    				}
//	        		
	        		curCell = curRow.createCell(cellIndex);
	        		curCell.setCellStyle(cellStyle3);
	        		curCell.setCellValue(pam);
	        	}
	        	//System.out.println();
	        }
			outStream = new FileOutputStream(filePath+ "/" + fileResultName);
			workbook.write(outStream);
			resultMap.put("code", "00");
			resultMap.put("codeNm", "정상");
		} catch (Exception e){
			resultMap.put("code", "21");
			resultMap.put("codeNm", "파일생성 오류");
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("resultSetMnspXlsxExcelCreate end");
		return resultMap;
	}
	
	/**
	 * 리서치 내역 엑셀 파일 생성
	 * @param mnspList
	 * @param reqMap
	 * @param sheetNum
	 * @param rowNum
	 * @return
	 */
	public static Map<String,Object> resultResearchXlsxExcelCreateV01(List<Map<String, Object>> resultList , Map<String,Object> reqMap, int sheetNum, int rowNum) {
		System.out.println("resultResearchXlsxExcelCreateV01("+ resultList.size()+") start");
		Map<String,Object> resultMap = new HashMap<String, Object>();
		FileOutputStream outStream = null;
		InputStream fis = null;	
		String p_type = StringUtil.nvl(reqMap.get("p_type"),"");
		System.out.println("p_type=>"+p_type);
		String resultFilePath = StringUtil.nvl(reqMap.get("resultFilePath"),"");
		String filePath = StringUtil.nvl(reqMap.get("filePath"),"");
		String fileName = StringUtil.nvl(reqMap.get("fileName"),"stock_form.xlsx");
		String fileResultName = StringUtil.nvl(reqMap.get("fileResultName"),"stock_result.xlsx");
		try {
			String year = StringUtil.nvl(reqMap.get("searchYear"),ComDateUtil.getYear());
			String quarter = StringUtil.nvl(reqMap.get("searchQuarter"),ComDateUtil.getQuarter());
			System.out.println(filePath+ "/" + fileName);
			fis = new FileInputStream(filePath+ "/" + fileName);
			XSSFWorkbook workbook = new XSSFWorkbook(fis); // xlsx 파일 Open
			
	        XSSFSheet sheet = workbook.getSheetAt(sheetNum);
	        XSSFRow curRow;
	        XSSFCell curCell;
	        String [] titles = {"순번","종목명","종목코드","제목","증권사","첨부URL","작성일"};
	        curRow = sheet.createRow(0);    // row 생성
	        for(int j = 0 ; j < titles.length ; j++) {
	        	curCell = curRow.createCell(j);
	        	XSSFCellStyle cs = workbook.createCellStyle();
	        	cs.setAlignment(HorizontalAlignment. CENTER);
	        	cs.setBorderRight(BorderStyle.THIN);
	    		cs.setBorderLeft(BorderStyle.THIN);
	    		cs.setBorderTop(BorderStyle.THIN);
	    		cs.setBorderBottom(BorderStyle.THIN);
	    		cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    		cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	curCell.setCellStyle(cs);
	        	curCell.setCellValue(titles[j]);
	        }
    		
	        
	        for (int rowIndex = rowNum; rowIndex < resultList.size()+rowNum; rowIndex++) {
	        	Map<String, Object> map = resultList.get(rowIndex);
	        	System.out.println(map);
	        	//Map<String, Object> voMap = BeanUtils.describe(vo);
	        	curRow = sheet.createRow(rowIndex+1);    // row 생성
	        	sheet.autoSizeColumn(rowIndex+1);
	        	XSSFCellStyle cellStyle2 = workbook.createCellStyle();
	        	cellStyle2.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle2.setBorderRight(BorderStyle.THIN);
	    		cellStyle2.setBorderLeft(BorderStyle.THIN);
	    		cellStyle2.setBorderTop(BorderStyle.THIN);
	    		cellStyle2.setBorderBottom(BorderStyle.THIN);
	    		cellStyle2.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 노란색
	    		cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFCellStyle cellStyle1 = workbook.createCellStyle();
	    		cellStyle1.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
	    		//테두리 선 (우,좌,위,아래)
	    		cellStyle1.setBorderRight(BorderStyle.THIN);
	    		cellStyle1.setBorderLeft(BorderStyle.THIN);
	    		cellStyle1.setBorderTop(BorderStyle.THIN);
	    		cellStyle1.setBorderBottom(BorderStyle.THIN);
	        	cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        	
	        	XSSFFont RedBold = workbook.createFont();
	        	RedBold.setColor(HSSFColor.RED.index);
	        	RedBold.setFontName("나눔고딕"); //글씨체
	        	
	        	XSSFFont BludBold = workbook.createFont();
	        	BludBold.setColor(HSSFColor.BLUE.index);
	        	BludBold.setFontName("나눔고딕"); //글씨체
	        	
	        	int cellIndex = 0;
	        	curCell = curRow.createCell(cellIndex++);
				XSSFCellStyle cs = workbook.createCellStyle();
				cs.setBorderRight(BorderStyle.THIN);
	    		cs.setBorderLeft(BorderStyle.THIN);
	    		cs.setBorderTop(BorderStyle.THIN);
	    		cs.setBorderBottom(BorderStyle.THIN);
				cs.setAlignment(HorizontalAlignment.CENTER);
				curCell.setCellStyle(cs);
        		curCell.setCellValue(rowIndex+1);
	        	
	        	for (int i = 0; i <  5; i++) {
	        		curCell = curRow.createCell(cellIndex++);
	        		String pam = StringUtil.nvl(map.get("parameter"+i),"").trim();
	        		String code = StringUtil.nvl(map.get("code"),"").trim();
	        		cs = workbook.createCellStyle();
	        		cs.setBorderRight(BorderStyle.THIN);
		    		cs.setBorderLeft(BorderStyle.THIN);
		    		cs.setBorderTop(BorderStyle.THIN);
		    		cs.setBorderBottom(BorderStyle.THIN);
	        		cs.setAlignment(HorizontalAlignment.CENTER);	        		
	        		curCell.setCellStyle(cs);
	        		
	        		if(i == 0) {//종목명
//	        			if(!"".equals(code)) {
//	        				pam = pam+"["+code+"]";
//	        			}
	        			curCell.setCellValue(pam);
	        			
	        			curCell = curRow.createCell(cellIndex++);
		        		curCell.setCellStyle(cs);
		        		curCell.setCellValue(code);
		        		
	        		}else if(i == 1) {//제목
	        			cs.setAlignment(HorizontalAlignment.LEFT);
	        			curCell.setCellValue(pam);
	        		}else if(i == 2) {//증권사
	        			cs.setAlignment(HorizontalAlignment.CENTER);
	        			curCell.setCellValue(pam);
	        		}else if(i == 3) {//첨부파일 URL
	        			pam = StringUtil.nvl(map.get("parameter"+i+"_href"),"").trim();
	        			cs = workbook.createCellStyle();
	        			cs.setBorderRight(BorderStyle.THIN);
			    		cs.setBorderLeft(BorderStyle.THIN);
			    		cs.setBorderTop(BorderStyle.THIN);
			    		cs.setBorderBottom(BorderStyle.THIN);
	        			cs.setAlignment(HorizontalAlignment.LEFT);
	        			curCell.setCellStyle(cs);
	        			curCell.setCellValue(pam);
	        		}else if(i == 4) {//작성일
	        			curCell.setCellValue(pam);
	        		}
	        		
//	        	    cs.setWrapText(true);

	        	}
	        }
	        System.out.println(resultFilePath+ "/" + fileResultName);
			outStream = new FileOutputStream(resultFilePath+ "/" + fileResultName);
			workbook.write(outStream);
			resultMap.put("code", "00");
			resultMap.put("codeNm", "정상");
		} catch (Exception e){
			resultMap.put("code", "21");
			resultMap.put("codeNm", "파일생성 오류");
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("resultResearchXlsxExcelCreateV01 end");
		return resultMap;
	}
}
