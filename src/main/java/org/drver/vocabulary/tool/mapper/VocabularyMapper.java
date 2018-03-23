package org.drver.vocabulary.tool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

import org.drver.vocabulary.tool.domain.Vocabulary;

public interface VocabularyMapper {

  @Select("<script>"
          + "SELECT type FROM vocabulary GROUP BY type"
          + "</script>")
  public List<String> getVocabularyTypeList();

  @Select("<script>"
          + "SELECT list FROM vocabulary WHERE type = #{type} GROUP BY list ORDER BY list asc"
          + "</script>")
  public List<Integer> getVocabularyListListByType(String type);

  @Select("<script>"
          + "SELECT * FROM vocabulary "
          + " WHERE 1 = 1 "
          + "<if test=\"id != null \">"
          + " AND id = #{id} "
          + "</if>"
          + "<if test=\"type != null and type != ''\">"
          + " AND type = #{type} "
          + "</if>"
          + "<if test=\"list != null \">"
          + " AND list = #{list} "
          + "</if>"
          + "<if test=\"word != null and word != ''\">"
          + " AND word = #{word} "
          + "</if>"
          + "<if test=\"meaning != null and meaning != ''\">"
          + " AND meaning = #{meaning} "
          + "</if>"
          + "<if test=\"status != null \">"
          + " AND status = #{status} "
          + "</if>"
          + "<if test=\"error != null \">"
          + " AND error = #{error} "
          + "</if>"
          + "</script>")
  @Results({
    @Result(id=true, column="id", property="id"),
    @Result(column="type", property="type"),
    @Result(column="list", property="list"),
    @Result(column="word", property="word"),
    @Result(column="meaning", property="meaning"),
    @Result(column="status", property="status"),
    @Result(column="error", property="error"),
  })
  public List<Vocabulary> selectVocabulary(Vocabulary query);

  @Insert("<script>"
          + "INSERT INTO vocabulary ( "
          + "<if test=\"type != null and type != ''\">"
          + " type, "
          + "</if>"
          + "<if test=\"list != null \">"
          + " list, "
          + "</if>"
          + "<if test=\"word != null and word != ''\">"
          + " word, "
          + "</if>"
          + "<if test=\"meaning != null and meaning != ''\">"
          + " meaning, "
          + "</if>"
          + "<if test=\"status != null \">"
          + " status, "
          + "</if>"
          + "<if test=\"error != null \">"
          + " error, "
          + "</if>"
          + " create_time "
          + ") "
          + " VALUES ( "
          + "<if test=\"type != null and type != ''\">"
          + " #{type}, "
          + "</if>"
          + "<if test=\"list != null \">"
          + " #{list}, "
          + "</if>"
          + "<if test=\"word != null and word != ''\">"
          + " #{word}, "
          + "</if>"
          + "<if test=\"meaning != null and meaning != ''\">"
          + " #{meaning}, "
          + "</if>"
          + "<if test=\"status != null \">"
          + " #{status}, "
          + "</if>"
          + "<if test=\"error != null \">"
          + " #{error}, "
          + "</if>"
          + " now() "
          + ")"
          + "</script>")
  public void insertVocabulary(Vocabulary vocabulary);

  @Update("<script>"
          + "UPDATE vocabulary "
          + "<set>"
          + "<if test=\"type != null and type != ''\">"
          + " type = #{type}, "
          + "</if>"
          + "<if test=\"list != null \">"
          + " list = #{list}, "
          + "</if>"
          + "<if test=\"word != null and word != ''\">"
          + " word = #{word}, "
          + "</if>"
          + "<if test=\"meaning != null and meaning != ''\">"
          + " meaning = #{meaning}, "
          + "</if>"
          + "<if test=\"status != null \">"
          + " status = #{status}, "
          + "</if>"
          + "<if test=\"error != null \">"
          + " error = #{error}, "
          + "</if>"
          + "</set>"
          + "<where>"
          + "<if test=\"id != null\">"
          + " and id = #{id} "
          + "</if>"
          + "</where>"
          + "</script>")
  public void updateVocabulary(Vocabulary vocabulary);

  @Select("<script>"
          + "SELECT * FROM vocabulary "
          + " WHERE error > 0 "
          + "</script>")
  @Results({
    @Result(id=true, column="id", property="id"),
    @Result(column="type", property="type"),
    @Result(column="list", property="list"),
    @Result(column="word", property="word"),
    @Result(column="meaning", property="meaning"),
    @Result(column="status", property="status"),
    @Result(column="error", property="error"),
  })
  public List<Vocabulary> selectError();
}
