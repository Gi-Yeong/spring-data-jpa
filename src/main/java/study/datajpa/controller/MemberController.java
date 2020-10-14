package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/members1/{id}")
    public String findMember(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id).get();

        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        int pageNumber = pageable.getPageSize();
        System.out.println("pageNumber = " + pageNumber);
        // 요청 URL ex) http://localhost/members?page=0&size=3&sort=id,desc&sort=username,desc
//        Page<Member> page = memberRepository.findAll(pageable);
        Page<Member> page = memberRepository.findPageAll(pageable);
//        return page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return page.map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            Team team = new Team("team");
            teamRepository.save(team);
            memberRepository.save(new Member("user" + i, i, team));
        }
    }
}
