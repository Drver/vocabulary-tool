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
import org.drver.vocabulary.tool.domain.Error;

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
          + "</script>")
  @Results({
    @Result(id=true, column="id", property="id"),
    @Result(column="type", property="type"),
    @Result(column="list", property="list"),
    @Result(column="word", property="word"),
    @Result(column="meaning", property="meaning"),
    @Result(column="status", property="status"),
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
          + "</set>"
          + "<where>"
          + "<if test=\"id != null\">"
          + " and id = #{id} "
          + "</if>"
          + "</where>"
          + "</script>")
  public void updateVocabulary(Vocabulary vocabulary);

  @Select("<script>"
          + "SELECT * FROM error "
          + " WHERE 1 = 1 "
          + "<if test=\"id != null \">"
          + " AND id = #{id} "
          + "</if>"
          + "<if test=\"vId != null \">"
          + " AND v_id = #{vId} "
          + "</if>"
          + "<if test=\"time != null \">"
          + " AND time = #{time} "
          + "</if>"
          + "</script>")
  @Results({
    @Result(id=true, column="id", property="id"),
    @Result(column="v_id", property="vId"),
    @Result(column="time", property="time"),
  })
  public List<Error> selectError(Error query);

  @Insert("<script>"
          + "INSERT INTO error ( "
          + "<if test=\"vId != null \">"
          + " v_id, "
          + "</if>"
          + "<if test=\"time != null \">"
          + " time, "
          + "</if>"
          + " create_time "
          + ") "
          + " VALUES ( "
          + "<if test=\"vId != null \">"
          + " #{vId}, "
          + "</if>"
          + "<if test=\"time != null \">"
          + " #{time}, "
          + "</if>"
          + " now() "
          + ")"
          + "</script>")
  public void insertError(Error error);

  @Update("<script>"
          + "UPDATE error "
          + "<set>"
          + "<if test=\"vId != null \">"
          + " v_id = #{vId}, "
          + "</if>"
          + "<if test=\"time != null \">"
          + " time = #{time}, "
          + "</if>"
          + "</set>"
          + "<where>"
          + "<if test=\"id != null\">"
          + " and id = #{id} "
          + "</if>"
          + "</where>"
          + "</script>")
  public void updateError(Error error);
}
