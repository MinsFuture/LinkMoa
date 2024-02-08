package com.knulinkmoa.domain.directory.entity;

import com.knulinkmoa.domain.directory.dto.request.DirectorySaveRequest;
import com.knulinkmoa.domain.member.entity.Member;
import com.knulinkmoa.domain.site.entity.Site;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Directory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "directory_id")
    private Long id;

    @Column(name = "directory_name")
    private String directoryName;

    @OneToMany(mappedBy = "directory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Site> siteList = new ArrayList<>();

    @Column(name = "parent_id")
    private Long parentId;


    @Builder
    public Directory(Long id, String directoryName, List<Site> siteList, Long parentId) {
        this.id = id;
        this.directoryName = directoryName;
        this.siteList = siteList;
        this.parentId = parentId;
    }

    public void update(DirectorySaveRequest request) {
        if (request.directoryName() != null) {
            this.directoryName = directoryName;
        }

    }
    public void addSite(Site site){
        siteList.add(site);
        site.setDirectory(this);
    }


}
