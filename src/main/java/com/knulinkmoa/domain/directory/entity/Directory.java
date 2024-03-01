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
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Directory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "directory_id")
    private Long id;

    @Column(name = "directory_name")
    private String directoryName;

    @OneToMany(mappedBy = "directory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Site> siteList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_directory_id")
    private Directory parentDirectory;

    @OneToMany(mappedBy = "parentDirectory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Directory> childDirectory = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Directory(Long id, String directoryName, List<Site> siteList, Directory parentDirectory, List<Directory> childDirectory, Member member) {
        this.id = id;
        this.directoryName = directoryName;
        this.siteList = siteList;
        this.parentDirectory = parentDirectory;
        this.childDirectory = childDirectory;
        this.member = member;
    }

    public void update(DirectorySaveRequest request) {
        if (request.directoryName() != null) {
            this.directoryName = request.directoryName();
        }
    }
    public void addSite(Site site){
        siteList.add(site);
        site.setDirectory(this);
    }

    public void addChildDirectory(Directory child) {
        this.childDirectory.add(child);
        child.setParentDirectory(this);
    }
}
