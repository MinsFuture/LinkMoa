package com.knulinkmoa.domain.directory.repository;

import com.knulinkmoa.domain.directory.entity.Directory;
import com.knulinkmoa.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    /**
     * 어떤 멤버의 모든 root directory 조회
     *
     * @param memberId
     *
     * @return 모든 root directory list 반환
     */
    @Query("SELECT d FROM Member m JOIN m.directories d WHERE m.id = :memberId AND d.parentDirectory IS NULL")
    List<Directory> findDirectoriesWithNullParentByMemberId(@Param("memberId") Long memberId);
}
