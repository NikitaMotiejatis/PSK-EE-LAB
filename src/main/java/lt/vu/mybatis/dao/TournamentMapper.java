package lt.vu.mybatis.dao;

import java.util.List;

import lt.vu.mybatis.model.Tournament;
import org.apache.ibatis.annotations.Param;
import org.mybatis.cdi.Mapper;

@Mapper
public interface TournamentMapper {

    int insert(Tournament record);

    Tournament selectByPrimaryKey(Integer id);

    List<Tournament> selectAll();

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKey(Tournament record);

    int insertPlayerTournament(@Param("tournamentId") Integer tournamentId,
                               @Param("playerId") Integer playerId);
}
