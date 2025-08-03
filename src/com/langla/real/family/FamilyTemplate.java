package com.langla.real.family;

import com.langla.data.Skill;
import com.langla.data.SkillClan;
import com.langla.real.item.Item;
import com.langla.real.player.Char;

import java.util.*;

/**
 * @author PKoolVN
 **/
public class FamilyTemplate {

    public int id;

    public String name;

    public FamilyInfo info = new FamilyInfo();
    public List<Family_Member> listMember = new ArrayList<Family_Member>();
    public List<Item> litsItem = new ArrayList<Item>();
    public List<SkillClan> listSkill = new ArrayList<>();
    public List<FamilyLog> dataLog = new ArrayList<FamilyLog>();
    public Map<Integer, Short> MapAi = new HashMap<>();

    public FamilyTemplate(){

    }
}
