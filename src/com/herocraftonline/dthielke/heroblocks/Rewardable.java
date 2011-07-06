package com.herocraftonline.dthielke.heroblocks;

import com.herocraftonline.dev.heroes.persistence.Hero;

public interface Rewardable {

    public boolean reward(Hero hero);

    public boolean isEligible(Hero hero);

}
