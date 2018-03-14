package org.drver.vocabulary.tool;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.drver.vocabulary.tool.mapper.VocabularyMapper;
import org.drver.vocabulary.tool.domain.Vocabulary;
import org.drver.vocabulary.tool.domain.Error;
import org.drver.vocabulary.tool.util.Util;

@SpringBootApplication
@EnableScheduling
public class VocabularyToolApplication implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(VocabularyToolApplication.class);

  private Scanner scanner = new Scanner(System.in);

  @Autowired
  private VocabularyMapper vocabularyMapper;

  public static void main(String[] args) {
    SpringApplication.run(VocabularyToolApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("VocabularyToolApplication started...");
    start();
  }

  private void start() {

    String useType = "";
    while(!useType.equals("R") && !useType.equals("N") && !useType.equals("I")) {
      Util.println("New(N), Review(R) or Import(I)?");
      useType = scanner.nextLine().toUpperCase();
      if(useType.equals("R")) {
        review();
      } else if (useType.equals("N")) {
        newV();
      } else if(useType.equals("I")) {
        importV();
      } else {

      }
    }
  }

  private void newV() {

    List<Vocabulary> vocabularyList = getVocabularyListByTypeAndList();
    Util.shuffle(vocabularyList);

    List<List<Vocabulary>> reviewListList = new ArrayList<List<Vocabulary>>();
    List<Vocabulary> reviewList = new ArrayList<Vocabulary>();
    int groupNum = 0;
    int reviewNum = 0;

    Util.println("");
    Util.println("new word:");
    for(int i = 0; i < vocabularyList.size(); i++) {
      Util.println("");
      Util.println(vocabularyList.get(i).getWord());
      scanner.nextLine();
      Util.println(vocabularyList.get(i).getMeaning());

      reviewList.add(vocabularyList.get(i));
      if(reviewList.size() >= 10 || i == vocabularyList.size() - 1) {
        reviewListList.add(reviewList);
        reviewList = new ArrayList<Vocabulary>();
        if(groupNum > 0) {
          groupReview(reviewListList.get(groupNum - 1));
          reviewNum++;
        }
        groupNum++;
      }
    }

    for(int i = reviewNum; i < groupNum; i++) {
      groupReview(reviewListList.get(i));
    }

  }

  private void review() {

    List<Vocabulary> vocabularyList = getVocabularyListByTypeAndList();
    Util.shuffle(vocabularyList);

    List<List<Vocabulary>> reviewListList = new ArrayList<List<Vocabulary>>();
    List<Vocabulary> reviewList = new ArrayList<Vocabulary>();

    for(int i = 0; i < vocabularyList.size(); i++) {

      reviewList.add(vocabularyList.get(i));
      if(reviewList.size() >= 10 || i == vocabularyList.size() - 1) {
        reviewListList.add(reviewList);
        reviewList = new ArrayList<Vocabulary>();
      }
    }

    for(int i = 0; i < reviewListList.size(); i++) {
      groupReview(reviewListList.get(i));
    }

  }

  private List<Vocabulary> getVocabularyListByTypeAndList() {

    //展示单词类型
    List<String> typeList = vocabularyMapper.getVocabularyTypeList();
    Util.println("");
    for(int i = 0; i < typeList.size(); i++) {
      Util.println("  " + typeList.get(i));
    }

    //选择类型
    String type = "";
    while(!typeList.contains(type)) {
      Util.println("Select a type:");
      type = scanner.nextLine();
    }

    //展示List
    List<Integer> listList = vocabularyMapper.getVocabularyListListByType(type);
    Util.println("");
    for(int i = 0; i < listList.size(); i++) {
      Util.println("  List " + listList.get(i));
    }

    //选择List
    Integer list = 0;
    while(!listList.contains(list)) {
      Util.println("Select a list:");
      list = Integer.valueOf(scanner.nextLine());
    }

    //按类型和章节获取全部单词并洗牌
    Vocabulary query = new Vocabulary();
    query.setType(type);
    query.setList(list);
    List<Vocabulary> vocabularyList = vocabularyMapper.selectVocabulary(query);
    return vocabularyList;
  }

  private void groupReview(List<Vocabulary> list) {

    Util.println("");
    Util.println("word 2 meaning. Y or N:");
    Util.shuffle(list);
    for(Vocabulary v : list) {
      word2Meaning(v);
      Util.println("");
    }
    Util.println("");
    Util.println("meaning 2 word. Input word:");
    Util.shuffle(list);
    for(Vocabulary v : list) {
      meaning2Word(v);
      Util.println("");
    }
    Util.println("");
    Util.println("group review finish.");
    Util.println("");
  }

  private void word2Meaning(Vocabulary v) {

    Util.println(v.getWord());
    String ok = "";
    while(!ok.equals("Y") && !ok.equals("N")) {

      ok = scanner.nextLine().toUpperCase();
      if(ok.equals("N")) {
        error(v);
      } else if(ok.equals("Y")) {

      } else {

      }
    }
    v.setStatus(1);
    vocabularyMapper.updateVocabulary(v);
    Util.println(v.getMeaning());
  }

  private void meaning2Word(Vocabulary v) {

    Util.println(v.getMeaning());
    String word = scanner.nextLine();
    if(!word.equals(v.getWord())) {
      error(v);
      Util.println(v.getWord());
    }
    v.setStatus(1);
    vocabularyMapper.updateVocabulary(v);
  }

  private void error(Vocabulary v) {
    Error query = new Error();
    query.setVId(v.getId());

    List<Error> errorList = vocabularyMapper.selectError(query);
    if(errorList.size() == 0) {
      query.setTime(1);
      vocabularyMapper.insertError(query);
    } else {
      Error error = errorList.get(0);
      error.setTime(error.getTime() + 1);
      vocabularyMapper.updateError(error);
    }
  }

  private void importV() {

    Util.println("Input file path:");
    String path = scanner.nextLine();

    File file = new File(path);
    if(!file.exists() || file.isDirectory()) {
      return;
    }

    List<String[]> dataList = null;
    try {
      dataList = getExcelData(file);
    } catch(IOException e) {
      e.printStackTrace();
    }
    Util.println("Input vocabulary type:");
    String type = scanner.nextLine();

    Util.println("Input list number:");
    Integer list = Integer.valueOf(scanner.nextLine());

    for(String[] data : dataList) {

      String word = data[0];
      String meaning = data[1];

      Vocabulary vocabulary = new Vocabulary();
      vocabulary.setType(type);
      vocabulary.setList(list);
      vocabulary.setWord(word);
      vocabulary.setMeaning(meaning);
      vocabulary.setStatus(0);

      vocabularyMapper.insertVocabulary(vocabulary);
    }
  }

  private List<String[]> getExcelData(File file) throws IOException{
    //获得Workbook工作薄对象
    Workbook workbook = getWorkBook(file);
    //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
    List<String[]> list = new ArrayList<String[]>();
    if(workbook != null){
      //获得第一个sheet工作表
      Sheet sheet = workbook.getSheetAt(0);
      //获得当前sheet的开始行
      int firstRowNum  = sheet.getFirstRowNum();
      //获得当前sheet的结束行
      int lastRowNum = sheet.getLastRowNum();
      //循环所有行
      for(int rowNum = firstRowNum;rowNum <= lastRowNum;rowNum++){
        //获得当前行
        Row row = sheet.getRow(rowNum);
        if(row == null){
            continue;
        }
        //获得当前行的开始列
        int firstCellNum = row.getFirstCellNum();
        //获得当前行的列数
        int lastCellNum = row.getLastCellNum();
        String[] cells = new String[row.getLastCellNum()];
        //循环当前行
        for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
            Cell cell = row.getCell(cellNum);
            cells[cellNum] = getCellValue(cell);
        }
        list.add(cells);
      }
    }
    return list;
  }

  private Workbook getWorkBook(File file) throws IOException {
    //获得文件名
    String fileName = file.getName();
    //创建Workbook工作薄对象，表示整个excel
    Workbook workbook = null;
    //获取excel文件的io流
    InputStream is = new FileInputStream(file);
    //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
    if(fileName.endsWith("xls")){
      //2003
      workbook = new HSSFWorkbook(is);
    }else if(fileName.endsWith("xlsx")){
      //2007 及2007以上
      workbook = new XSSFWorkbook(is);
    }
    return workbook;
  }

  private String getCellValue(Cell cell){
    String cellValue = "";
    if(cell == null){
        return cellValue;
    }
    //判断数据的类型
    switch (cell.getCellType()){
      case Cell.CELL_TYPE_NUMERIC: //数字
          cellValue = String.valueOf(cell.getStringCellValue());
          break;
      case Cell.CELL_TYPE_STRING: //字符串
          cellValue = String.valueOf(cell.getStringCellValue());
          break;
      case Cell.CELL_TYPE_BOOLEAN: //Boolean
          cellValue = String.valueOf(cell.getBooleanCellValue());
          break;
      case Cell.CELL_TYPE_FORMULA: //公式
          cellValue = String.valueOf(cell.getCellFormula());
          break;
      case Cell.CELL_TYPE_BLANK: //空值
          cellValue = "";
          break;
      case Cell.CELL_TYPE_ERROR: //故障
          cellValue = "非法字符";
          break;
      default:
          cellValue = "未知类型";
          break;
    }
    return cellValue;
  }
}
