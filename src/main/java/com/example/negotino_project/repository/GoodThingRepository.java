package com.example.negotino_project.repository;

import com.example.negotino_project.entities.GoodThing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GoodThingRepository extends JpaRepository<GoodThing, Integer>
{
    List<GoodThing> findAllByApprovedFalse();
    List<GoodThing> findAllByApprovedTrue();
    List<GoodThing> findTop10ByApprovedTrueOrderByIdDesc();

    @Query("SELECT u.author,count(*) as v FROM GoodThing u WHERE u.approved=1 group by u.author order by v DESC")
    List<Object[]> getTopThreeAuthors();

}
