import type { PlayerStatistic } from '../types/cricket';

interface TeamRoster {
  batters: string[];
  bowlers: string[];
}

const TEAM_ROSTERS: Record<string, TeamRoster> = {
  canada: {
    batters: ['Aaron Johnson', 'Navneet Dhaliwal', 'Pargat Singh', 'Nicholas Kirton', 'Harsh Thaker', 'Shreyas Movva', 'Saad Bin Zafar'],
    bowlers: ['Kaleem Sana', 'Dillon Heyliger', 'Jeremy Gordon', 'Junaid Siddiqui', 'Saad Bin Zafar', 'Pargat Singh'],
  },
  nepal: {
    batters: ['Kushal Bhurtel', 'Aasif Sheikh', 'Rohit Paudel', 'Anil Sah', 'Dipendra Singh Airee', 'Kushal Malla', 'Gulshan Jha'],
    bowlers: ['Sompal Kami', 'Karan KC', 'Sandeep Lamichhane', 'Gulshan Jha', 'Lalit Rajbanshi', 'Dipendra Singh Airee'],
  },
  india: {
    batters: ['Rohit Sharma', 'Yashasvi Jaiswal', 'Virat Kohli', 'Suryakumar Yadav', 'Hardik Pandya', 'Rishabh Pant', 'Ravindra Jadeja'],
    bowlers: ['Jasprit Bumrah', 'Mohammed Siraj', 'Arshdeep Singh', 'Kuldeep Yadav', 'Ravindra Jadeja', 'Hardik Pandya'],
  },
  australia: {
    batters: ['Travis Head', 'David Warner', 'Mitchell Marsh', 'Glenn Maxwell', 'Marcus Stoinis', 'Tim David', 'Matthew Wade'],
    bowlers: ['Mitchell Starc', 'Josh Hazlewood', 'Pat Cummins', 'Adam Zampa', 'Marcus Stoinis', 'Glenn Maxwell'],
  },
  england: {
    batters: ['Jos Buttler', 'Phil Salt', 'Will Jacks', 'Jonny Bairstow', 'Harry Brook', 'Liam Livingstone', 'Moeen Ali'],
    bowlers: ['Jofra Archer', 'Mark Wood', 'Adil Rashid', 'Sam Curran', 'Reece Topley', 'Moeen Ali'],
  },
  pakistan: {
    batters: ['Mohammad Rizwan', 'Babar Azam', 'Fakhar Zaman', 'Usman Khan', 'Iftikhar Ahmed', 'Shadab Khan', 'Imad Wasim'],
    bowlers: ['Shaheen Afridi', 'Naseem Shah', 'Haris Rauf', 'Mohammad Amir', 'Shadab Khan', 'Abrar Ahmed'],
  },
  southafrica: {
    batters: ['Quinton de Kock', 'Reeza Hendricks', 'Aiden Markram', 'Heinrich Klaasen', 'David Miller', 'Tristan Stubbs', 'Marco Jansen'],
    bowlers: ['Kagiso Rabada', 'Anrich Nortje', 'Marco Jansen', 'Keshav Maharaj', 'Ottneil Baartman', 'Tabraiz Shamsi'],
  },
  newzealand: {
    batters: ['Finn Allen', 'Devon Conway', 'Kane Williamson', 'Daryl Mitchell', 'Glenn Phillips', 'Mark Chapman', 'Mitchell Santner'],
    bowlers: ['Trent Boult', 'Tim Southee', 'Lockie Ferguson', 'Mitchell Santner', 'Matt Henry', 'Ish Sodhi'],
  },
  westindies: {
    batters: ['Brandon King', 'Johnson Charles', 'Nicholas Pooran', 'Rovman Powell', 'Sherfane Rutherford', 'Andre Russell', 'Romario Shepherd'],
    bowlers: ['Akeal Hosein', 'Alzarri Joseph', 'Gudakesh Motie', 'Romario Shepherd', 'Andre Russell', 'Roston Chase'],
  },
  srilanka: {
    batters: ['Pathum Nissanka', 'Kusal Mendis', 'Kamindu Mendis', 'Dhananjaya de Silva', 'Charith Asalanka', 'Angelo Mathews', 'Dasun Shanaka'],
    bowlers: ['Wanindu Hasaranga', 'Maheesh Theekshana', 'Matheesha Pathirana', 'Nuwan Thushara', 'Dushmantha Chameera'],
  },
  bangladesh: {
    batters: ['Litton Das', 'Tanzid Hasan', 'Najmul Hossain Shanto', 'Towhid Hridoy', 'Shakib Al Hasan', 'Mahmudullah', 'Jaker Ali'],
    bowlers: ['Taskin Ahmed', 'Mustafizur Rahman', 'Tanzim Hasan Sakib', 'Rishad Hossain', 'Shakib Al Hasan'],
  },
  afghanistan: {
    batters: ['Rahmanullah Gurbaz', 'Ibrahim Zadran', 'Gulbadin Naib', 'Azmatullah Omarzai', 'Mohammad Nabi', 'Najibullah Zadran', 'Rashid Khan'],
    bowlers: ['Fazalhaq Farooqi', 'Naveen-ul-Haq', 'Rashid Khan', 'Noor Ahmad', 'Mohammad Nabi'],
  },
  oman: {
    batters: ['Kashyap Prajapati', 'Pratik Athavale', 'Aqib Ilyas', 'Zeeshan Maqsood', 'Khalid Kail', 'Ayaan Khan'],
    bowlers: ['Bilal Khan', 'Kaleemullah', 'Shakeel Ahmed', 'Fayyaz Butt', 'Ayaan Khan'],
  },
  scotland: {
    batters: ['George Munsey', 'Michael Jones', 'Brandon McMullen', 'Richie Berrington', 'Matthew Cross', 'Michael Leask'],
    bowlers: ['Brad Wheal', 'Chris Sole', 'Mark Watt', 'Safyaan Sharif', 'Chris Greaves'],
  },
  netherlands: {
    batters: ['Michael Levitt', 'Max O\'Dowd', 'Vikramjit Singh', 'Sybrand Engelbrecht', 'Scott Edwards', 'Bas de Leede'],
    bowlers: ['Logan van Beek', 'Paul van Meekeren', 'Tim Pringle', 'Vivian Kingma', 'Aryan Dutt'],
  },
  usa: {
    batters: ['Steven Taylor', 'Monank Patel', 'Andries Gous', 'Aaron Jones', 'Nitish Kumar', 'Corey Anderson'],
    bowlers: ['Saurabh Netravalkar', 'Ali Khan', 'Harmeet Singh', 'Jasdeep Singh', 'Nosthush Kenjige'],
  },
};

export function getRosterForTeam(teamName: string): TeamRoster {
  const clean = (teamName || '').toLowerCase().replace(/[^a-z]/g, '');
  for (const key of Object.keys(TEAM_ROSTERS)) {
    if (clean.includes(key)) {
      return TEAM_ROSTERS[key];
    }
  }

  // Generates generic country-appropriate squad
  const prefix = (teamName || 'Team').substring(0, 3).toUpperCase();
  return {
    batters: [
      `${prefix} Opener 1`,
      `${prefix} Opener 2`,
      `${prefix} Top Order`,
      `${prefix} Middle Order`,
      `${prefix} Batter 5`,
      `${prefix} All-Rounder`,
    ],
    bowlers: [
      `${prefix} Pacer 1`,
      `${prefix} Pacer 2`,
      `${prefix} Spinner 1`,
      `${prefix} Spinner 2`,
      `${prefix} All-Rounder`,
    ],
  };
}

export function generateRealisticMatchScorecard(
  battingTeam: string,
  bowlingTeam: string,
  totalRuns: number,
  wickets: number,
  totalOvers: number
): { batsmen: PlayerStatistic[]; bowlers: PlayerStatistic[]; partnership: string } {
  const battingRoster = getRosterForTeam(battingTeam);
  const bowlingRoster = getRosterForTeam(bowlingTeam);

  const safeWickets = Math.max(0, Math.min(wickets, 10));
  const safeRuns = Math.max(0, totalRuns);
  const safeOvers = Math.max(0.1, totalOvers);
  const totalBalls = Math.round(Math.floor(safeOvers) * 6 + (safeOvers % 1) * 10);

  const batsmen: PlayerStatistic[] = [];
  const activeBattersCount = Math.min(safeWickets + 2, battingRoster.batters.length);

  // Distribute runs and balls across active batsmen
  let runsRemaining = safeRuns;
  let ballsRemaining = totalBalls;

  const dismissalBowlers = bowlingRoster.bowlers;

  for (let i = 0; i < activeBattersCount; i++) {
    const isOut = i < safeWickets;
    const name = battingRoster.batters[i] || `Player ${i + 1}`;

    let bRuns = 0;
    let bBalls = 0;

    if (i === activeBattersCount - 1) {
      bRuns = runsRemaining;
      bBalls = ballsRemaining;
    } else {
      const share = Math.max(0.15, Math.min(0.4, (activeBattersCount - i) / (activeBattersCount * 1.5)));
      bRuns = Math.max(0, Math.round(runsRemaining * share));
      bBalls = Math.max(1, Math.round(ballsRemaining * share));
      runsRemaining -= bRuns;
      ballsRemaining -= bBalls;
    }

    if (bRuns < 0) bRuns = 0;
    if (bBalls < 1) bBalls = 1;

    const fours = Math.floor(bRuns / 9);
    const sixes = Math.floor((bRuns - fours * 4) / 10);
    const strikeRate = Number(((bRuns / bBalls) * 100).toFixed(2));

    let dismissalInfo = 'not out';
    if (isOut) {
      const bowl = dismissalBowlers[i % dismissalBowlers.length];
      dismissalInfo = i % 2 === 0 ? `c Fielder b ${bowl}` : `lbw b ${bowl}`;
    }

    batsmen.push({
      playerName: name,
      dismissalInfo,
      runs: bRuns,
      balls: bBalls,
      fours,
      sixes,
      strikeRate,
      role: 'BATSMAN',
    });
  }

  // Generate Bowlers
  const bowlersList: PlayerStatistic[] = [];
  const numBowlers = Math.min(5, bowlingRoster.bowlers.length);
  const oversPerBowler = Number((safeOvers / numBowlers).toFixed(1));
  let wktsRemaining = safeWickets;
  let runsConcededRemaining = safeRuns;

  for (let j = 0; j < numBowlers; j++) {
    const bName = bowlingRoster.bowlers[j] || `Bowler ${j + 1}`;
    let bW = 0;
    if (j === numBowlers - 1) {
      bW = wktsRemaining;
    } else {
      bW = wktsRemaining > 0 ? (j % 2 === 0 ? 1 : 0) : 0;
      wktsRemaining -= bW;
    }

    const bO = j === numBowlers - 1 ? Number((safeOvers - oversPerBowler * (numBowlers - 1)).toFixed(1)) : oversPerBowler;
    const safeBO = Math.max(0.1, bO);

    let bR = Math.round(runsConcededRemaining / (numBowlers - j));
    if (j === numBowlers - 1) bR = runsConcededRemaining;
    runsConcededRemaining -= bR;

    const econ = Number((bR / safeBO).toFixed(2));

    bowlersList.push({
      playerName: bName,
      oversBowled: safeBO,
      maidens: j === 0 || j === 2 ? 1 : 0,
      runsConceded: bR,
      wickets: bW,
      economy: econ,
      role: 'BOWLER',
    });
  }

  // Calculate partnership for current not out batters
  const notOuts = batsmen.filter((b) => b.dismissalInfo === 'not out');
  const partRuns = notOuts.reduce((acc, curr) => acc + (curr.runs || 0), 0);
  const partBalls = notOuts.reduce((acc, curr) => acc + (curr.balls || 0), 0);
  const partnership = `${partRuns} (${partBalls})`;

  return { batsmen, bowlers: bowlersList, partnership };
}
