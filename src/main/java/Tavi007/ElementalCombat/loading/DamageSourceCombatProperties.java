package Tavi007.ElementalCombat.loading;

import Tavi007.ElementalCombat.ElementalCombat;

public class DamageSourceCombatProperties {
	public static final DamageSourceCombatProperties EMPTY = new DamageSourceCombatProperties();

	private final String attack_style;
	private final String attack_element;
	
	public DamageSourceCombatProperties() {
		this.attack_style = "basic";
		this.attack_element = "natural";
	}
	
	public DamageSourceCombatProperties(String attack_style, String attack_element) {
		   if (attack_style.isEmpty()) {
			   this.attack_style = ElementalCombat.DEFAULT_STYLE;
		   }
		   else {
			   this.attack_style = attack_style;
		   }
		   if (attack_element.isEmpty()) {
			   this.attack_element = ElementalCombat.DEFAULT_ELEMENT;
		   }
		   else {
			   this.attack_element = attack_element;
		   }
	}

	public String getAttackStyle() {return this.attack_style;}
	public String getAttackElement() {return this.attack_element;}
}
