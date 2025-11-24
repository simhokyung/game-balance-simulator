# ⚔️ 우테코 자유과제 - Game Balance Simulator
콘솔 기반으로 여러 캐릭터 빌드를 수천/수만 판씩 1:1로 자동 전투시키고, **승률·턴 수·남은 HP 비율**을 집계하여
각 캐릭터가 **OP인지, 약체인지, 밸런스가 맞는지**를 평가하는 시뮬레이터이다.
- 작은 단위의 책임으로 도메인을 분리하고 **TDD**로 검증하면서, “어떤 상태를 **밸런스가 잘 잡힌 상태**라고 볼 것인지"를 스스로 정의하고 구현하는 것이 목표이다.
- 하드 코딩 금지, 상수 관리, 구현 순서 컨벤션, 단일 책임, indent ≤ 2, `else/switch/3항` 금지 원칙을 따른다.
---
## ✨ 구현된 기능 개요
- 1:1 턴제 전투 규칙 엔진 구현
- **밸런스형 전투 캐릭터 6종**(브루저 / 어쌔신 / 탱커 / 스피드스터 / 서스테이너 / 하이브리드) 제공
- 모든 캐릭터 조합에 대한 **전체 리그 시뮬레이션 + 밸런스 분석**
- 각 캐릭터 간의 **매치업 승률 매트릭스** 출력
- **Skill 훅(onTurnStart / onBeforeAttack / onAfterAttack / onDamaged)** 기반의 확장 가능한 스킬 시스템
---
## 📥 입출력 명세
본 프로젝트는 **콘솔** 기반으로 시작한다.

### ▫ 입력
### 0) **모드 선택**
```plaintext
=== Game Balance Simulator ===

모드를 선택하세요:
1: 미리 준비된 프리셋 시나리오 사용
2: 직접 캐릭터 스펙 입력
메뉴 번호를 입력하세요 (1-2):
```
### 1) **프리셋 모드** 
프리셋 모드(1번)를 선택하면, 미리 정의된 1대1 밸런스형 전투 캐릭터 6종을 사용한다.
```plaintext
[프리셋 모드] 1대1 밸런스형 전투 캐릭터 6종을 사용합니다.
1: 캐릭터별 스탯/스킬 설명 보기
2: 바로 전투 시뮬레이션 시작
번호를 입력하세요 (1-2):
```

#### 1-1) **캐릭터별 스탯/스킬 설명 보기**
1번을 선택하면 아래 6개 캐릭터 중 하나를 골라 상세 설명을 확인할 수 있다.
1. Bruiser
2. Assassin 
3. Tank 
4. Speedster
5. Sustainer
6. Hybrid

```plaintext
설명을 보고 싶은 캐릭터를 선택하세요:
1: Bruiser
2: Assassin
3: Tank
4: Speedster
5: Sustainer
6: Hybrid
0: 뒤로가기
번호를 입력하세요 (0-6):
```

- 번호를 고르면 해당 캐릭터의 **기본 스탯**과 **스킬 설명 3개(패시브 1 + 액티브 2)**가 출력된다.
- 다시 번호 선택 화면으로 돌아오며, `0`을 입력하면 **전투 시작 화면**으로 돌아간다.
(아래는 예시)

```plaintext
[Assassin]
기본 스탯: HP=900, ATK=120, DEF=30, SPD=40, CRIT=0.30

▶ 역할: 짧은 턴 안에 승부 보는 폭딜 암살자

- 패시브: 약점 포착
  · 적의 현재 HP가 일정 이하(예: 30%)일 때,
    그 적을 공격하면 추가 피해를 준다.

- 스킬1: 선제 일격
  · 전투에서 이 암살자가 날리는 '첫 번째 공격'에만 발동.
  · 그 한 번의 공격에 큰 추가 피해를 실어 보낸다.

- 스킬2: 연막(Shadow Evasion)
  · 큰 피해를 받아 체력이 위험 구간(예: 30% 이하)으로 떨어질 때 발동.
  · 그 공격의 일부 피해를 되돌려 받아 피해를 줄이고,
    다음 1턴 동안 들어오는 피해를 크게 줄이는 효과가 생긴다.
  · 발동 후에는 몇 턴 동안 쿨타임이 돌아간다.
```

0번(뒤로가기)로 돌아가서 다시 캐릭터를 보거나, 시뮬레이션을 시작할 수 있다.

#### 1-2) 전투 시작 설정

프리셋 설명을 보았거나, 바로 전투를 선택하면 **라운드 수**를 입력한다.

```plaintext
각 캐릭터 조합당 전투 횟수(라운드 수)를 입력하세요 (예: 1000):
1000
```

이후 전체 리그 시뮬레이션이 수행되고, 밸런스 리포트가 출력된다.  
마지막에는 각 캐릭터간의 승률상세 확인 여부를 선택한다.

```plaintext
캐릭터별 매치업 승률 상세를 확인하시겠습니까?
1: 예
2: 아니오
번호를 입력하세요 (1-2):
```

---

### 2) 직접 캐릭터 입력 모드 

2번을 선택하면 캐릭터 스펙을 직접 입력한다.

#### 2-1) 캐릭터 수 입력

```plaintext
[직접 입력 모드] 시뮬레이션할 캐릭터를 입력해 주세요.

시뮬레이션할 캐릭터 수를 입력하세요 (최소 2명):
3
```

#### 2-2) 캐릭터 스탯 입력

각 캐릭터는 **한 줄**에 아래 형식으로 입력한다.

```plaintext
캐릭터 #1 정보를 입력하세요 (형식: 이름 HP ATK DEF SPD CRIT):
예시: Warrior 120 40 20 15 0.2
Warrior 120 40 20 15 0.2

캐릭터 #2 정보를 입력하세요 (형식: 이름 HP ATK DEF SPD CRIT):
Assassin 90 55 15 25 0.3

캐릭터 #3 정보를 입력하세요 (형식: 이름 HP ATK DEF SPD CRIT):
Tank 200 30 40 10 0.1
```

- `HP, ATK, DEF, SPD`는 **양의 정수**
- `CRIT`은 0.0 ~ 1.0 범위 실수

형식이 잘못되면 `[ERROR]` 메시지를 출력하고 **해당 캐릭터만 재입력** 받는다.

#### 2-3) 라운드 수 입력

```plaintext
각 캐릭터 조합당 전투 횟수(라운드 수)를 입력하세요 (예: 1000):
1000
```

이후 흐름은 프리셋 모드와 동일하게:

- 전체 리그 시뮬레이션
- 캐릭터별 통계/밸런스 평가 출력
- 매치업 승률 매트릭스 출력 여부 선택

---

## 📤 출력 예시

### 1) 전체 밸런스 리포트 

- 각 캐릭터별 티어/승률/전적(W/L/D)/ 경기 수
- 캐릭터별 밸런스 평가

```plaintext
========================================
         Game Balance Report
========================================
이름 | 티어 | 승률 | 전적(W/L/D) | 경기 수
----------------------------------------
Sustainer | OVERPOWERED  | 0.72 | 36185/13815/0 | 50000
Assassin  | OVERPOWERED  | 0.61 | 30623/19377/0 | 50000
Hybrid    | BALANCED     | 0.59 | 29253/20747/0 | 50000
Speedster | BALANCED     | 0.55 | 27557/22443/0 | 50000
Tank      | UNDERPOWERED | 0.38 | 18960/31040/0 | 50000
Bruiser   | UNDERPOWERED | 0.15 | 7422/42578/0  | 50000

[밸런스 평가 기준]
- 승률 0.40 미만  → UNDERPOWERED (버프 필요)
- 승률 0.40~0.60 → BALANCED
- 승률 0.60 초과  → OVERPOWERED (너프 필요)

----- 상세 사유 -----
[Sustainer] 승률이 60% 이상(0.72)으로 높아 OVERPOWERED로 분류합니다.
[Assassin] 승률이 60% 이상(0.61)으로 높아 OVERPOWERED로 분류합니다.
[Hybrid] 승률이 중간 구간(40%~60%)에 위치하여 BALANCED로 분류합니다. (승률=0.59)
[Speedster] 승률이 중간 구간(40%~60%)에 위치하여 BALANCED로 분류합니다. (승률=0.55)
[Tank] 승률이 40% 이하(0.38)로 낮아 UNDERPOWERED로 분류합니다.
[Bruiser] 승률이 40% 이하(0.15)로 낮아 UNDERPOWERED로 분류합니다.
```

### 2) 매치업 승률 매트릭스 

```plaintext
캐릭터별 매치업 승률 상세를 확인하시겠습니까?
1: 예
2: 아니오
번호를 입력하세요 (1-2): 1

========================================
       Matchup Winrate Matrix
 (행 캐릭터가 열 캐릭터를 이길 확률)
========================================
                 Bruiser    Assassin        Tank   Speedster   Sustainer      Hybrid
     Bruiser           -        0.37        0.51        0.27        0.93        0.80
    Assassin        0.63           -        0.39        0.38        0.24        0.72
        Tank        0.49        0.61           -        0.86        0.28        0.08
   Speedster        0.73        0.62        0.14           -        0.54        0.47
   Sustainer        0.07        0.76        0.72        0.46           -        0.53
      Hybrid        0.20        0.28        0.92        0.53        0.47           -
※ 값은 행 캐릭터가 열 캐릭터를 상대로 승리한 비율입니다. (무승부 포함)
========================================
```
- `1`을 선택하면 **캐릭터 vs 캐릭터 승률 테이블**이 출력된다.
- `2`를 선택하면 테이블 없이 프로그램이 종료된다.

---

## 🗡️ 전투 규칙 요약
전투는 항상 **1:1**로 진행된다.
### 1) 속도 게이지 기반 턴 시스템

- 각 캐릭터는 전투 중 **속도 게이지**를 가진다.
- 매 루프마다 `게이지 += SPD`씩 채운다.
- 게이지가 100 이상이 되는 순간, 그 캐릭터가 행동한다.
- 두 캐릭터 게이지가 동시에 100 이상이 되면:
    - 게이지가 더 큰 쪽이 우선
    - 완전히 동일하면 SPD가 높은 쪽, 그래도 같으면 첫 번째 캐릭터 우선

### 2) 기본 데미지 공식
`DefaultDamageCalculator`:
```java
double attack = attacker.getCharacter().getAttack();
double defense = defender.getCharacter().getDefense();

// 비율형 데미지 계산
double baseDamageDouble = attack * (attack / (attack + defense));

int baseDamage = (int) baseDamageDouble;
if (baseDamage < 1) baseDamage = 1;
// 치명타 판정
boolean isCritical = randomProvider.nextDouble() < attacker.getCharacter().getCritChance();
if (isCritical) baseDamage *= 1.5;

return (int) baseDamage;
```
- 공격력은 캐릭터의 ATK, 방어력은 DEF 스탯을 사용.
- 데미지 공식은 `ATK * (ATK / (ATK + DEF))` 형태의 비율형 공식을 적용한다.
- 치명타 확률은 캐릭터의 CRIT 스탯을 사용
- 치명타 데미지=`baseDamage`*1.5


### 3) 스킬 훅

전투 도중 스킬은 다음 훅에 반응한다.

```java
void onTurnStart(SkillContext ctx);           // 자신의 턴이 시작될 때
void onBeforeAttack(SkillContext ctx);        // 공격 직전
void onAfterAttack(SkillContext ctx, int damageDealt); // 공격 후
void onDamaged(SkillContext ctx, int damageTaken);     // 피해를 입은 직후
```

- `SkillContext`는 `self`, `opponent`, `firstAttackInBattle` 등의 정보를 제공한다.
- 모든 스킬은 이 훅들을 이용해 **추가 피해, 피해 감소, 회복, 상태 추적(쿨타임/스택)** 등을 구현한다.


### 4) 전투 종료 조건

- 한쪽 HP ≤ 0 → 즉시 종료, 승/패 결정
- 최대 턴 수(100턴) 초과 → **무승부 처리**

---

## 🤺 내장 프리셋: 1대1 밸런스형 전투 캐릭터 6종

프리셋 모드에서 사용하는 6개 캐릭터는 다음과 같이 설계되어 있다.  
(스탯은 `balance.preset.PresetCharacters` 참고)

### 1) Bruiser – “맞을수록 강해지는 근접 브루저”

- **기본 컨셉**
    - 체력·공격력을 모두 갖춘 근접 파이터
    - 맞다 보면 분노가 쌓이고, 특정 타이밍에 큰 한 방을 넣는 구조

- **패시브 – Rage Passive** (`BruiserRagePassiveSkill`)
    - 피해를 받을 때마다 분노 스택 +1 (최대 10)
    - 실제 추가 피해는 분노 폭발 스킬에서 사용

- **스킬1 – Raging Blow** (`BruiserRagingBlowSkill`)
    - 턴 시작 시 쿨타임 감소
    - 공격 직전, **분노 스택 ≥ 7 & 쿨타임 0**이면 이번 공격에 분노 폭발 활성화
    - 공격 후, 활성화 상태라면
        - 스택당 +1% 추가 피해
        - 스택 초기화 + 5턴 쿨타임

- **스킬2 – Second Wind** (`BruiserSecondWindSkill`)
    - 전투 중 자신의 HP가 **처음으로 25% 이하**가 될 때 1회 발동
        - 최대 HP의 15% 즉시 회복
        - 이후 1턴 동안 받은 피해의 5%를 되돌려 받아 추가 생존력 확보

---

### 2) Assassin – “짧은 턴 안에 승부 보는 폭딜 암살자”

- **기본 컨셉**
    - 초반/짧은 턴 안에 상대를 녹이는 폭딜형
    - 대신 한 번 타이밍을 놓치면 평범한 딜러 수준으로 떨어지는 리스크

- **패시브 – Exposed Weakness** (`AssassinExposedWeaknessSkill`)
    - 적의 현재 HP가 30% 이하일 때,  
      이번 공격으로 입힌 피해의 5%를 **추가 피해**로 한 번 더 넣는다.
    - 마무리 타이밍에 위력이 올라가는 구조

- **스킬1 – Opening Strike** (`AssassinOpeningStrikeSkill`)
    - 전투 내 **“이 암살자의 첫 공격”**에 한해 발동
    - 그 공격이 입힌 피해의 40%만큼 추가 피해
    - 한 번 발동 후에는 해당 전투에서 다시 발동하지 않는다.

- **스킬2 – Shadow Evasion** (`AssassinShadowEvasionSkill`)
    - HP가 30% 이하로 떨어지는 피해를 입을 때:
        - 이번 피해의 30%를 즉시 되돌려 받아 피해 감소
        - 다음 1턴 동안 맞을 때마다 피해의 50%를 기대값 기준으로 되돌려 받는다.
        - 이후 5턴 쿨타임

---

### 3) Tank – “장기전 방패 + 반격 탱커”

- **기본 컨셉**
    - 들어오는 피해를 크게 줄이고, 때때로 반격으로 손해를 돌려주는 방어형

- **패시브 – Heavy Armor** (`TankHeavyArmorSkill`)
    - 모든 피해에 대해 `고정 5 + 피해의 5%`만큼 상쇄
    - 구현상, 받은 피해 직후 heal을 통해 **실질 피해 감소**로 근사

- **스킬1 – Fortify** (`TankFortifySkill`)
    - 자기 턴 시작 시, 버프가 없고 쿨타임 0이면 자동 발동
    - 2턴 동안 맞는 피해의 10%를 되돌려 받아 추가 피해 감소
    - 5턴 쿨타임

- **스킬2 – Spiked Counter** (`TankSpikedCounterSkill`)
    - HP 50% 이상 & 쿨타임 0 상태에서 큰 피해를 맞으면
        - 받은 피해의 15%를 즉시 반사
        - 3턴 쿨타임
    - 단단할 때 함부로 두들기면 손해를 보는 형태

---

### 4) Speedster – “턴 이득과 회피를 노리는 스피드형”

- **기본 컨셉**
    - SPD가 높아 자주 행동하고, 일정 간격으로 추가 타격/피해 감소를 노리는 캐릭터

- **패시브 – Quick Footwork** (`SpeedsterQuickFootworkSkill`)
    - 매 공격 후, 준 피해의 5%를 추가 피해로 더 넣는다.
    - **“속도가 높아 평균적으로 더 많은 피해를 누적한다”**는 컨셉을 수치로 근사

- **스킬1 – Quick Combo** (`SpeedsterQuickComboSkill`)
    - 자신의 행동 횟수를 세다가,
        - 행동 2회 이상 & 쿨타임 0이면 이번 공격에 한해  
          준 피해의 10%를 추가 피해로 더한다.
        - 발동 후 4턴 쿨타임

- **스킬2 – Precise Dodge** (`SpeedsterPreciseDodgeSkill`)
    - 직전 HP의 25% 이상에 해당하는 큰 피해를 맞으면:
        - 그 피해의 5%를 즉시 회복
        - **다음 한 번 더 맞을 때**, 그 피해의 5%를 추가로 회복
        - 4턴 쿨타임

---

### 5) Sustainer – “흡혈과 재생으로 버티는 장기전형”

- **기본 컨셉**
    - 한 방 폭딜은 약하지만, 흡혈과 재생으로 오래 버티면서 천천히 이득을 본다.

- **패시브 – Lifesteal** (`SustainerLifestealSkill`)
    - 자신이 준 피해의 5%만큼 체력을 회복

- **스킬1 – Healing Aura** (`SustainerRegenSkill`)
    - HP 비율이 30% 이하일 때, 재생 효과와 쿨타임이 모두 비활성 상태라면:
        - 3턴 동안 턴 시작 시마다 최대 HP의 5% 회복
        - 이후 5턴 쿨타임

- **스킬2 – Blood Frenzy** (`SustainerBloodFrenzySkill`)
    - HP 비율이 30% 이하이고 쿨타임 0일 때:
        - 이번 공격으로 준 피해의 15%를 추가로 회복
        - 4턴 쿨타임

---

### 6) Hybrid – “상황에 따라 공격/방어 모드를 바꾸는 만능형”

- **기본 컨셉**
    - HP가 높을 땐 공격형, 낮을 땐 방어형으로 작동하는 유연한 캐릭터

- **패시브 – Balanced Instinct** (`HybridBalancedInstinctSkill`)
    - HP ≥ 50%: 공격 모드 → 공격 시 준 피해의 5%만큼 추가 피해
    - HP < 50%: 방어 모드 → 피해를 입을 때마다 피해의 5%만큼 회복

- **스킬1 – Tactical Focus** (`HybridTacticalFocusSkill`)
    - 턴 시작 시 일정 조건에서 자동으로 “집중 모드” 활성
    - 집중 상태에서는 공격 후 준 피해의 10%만큼 추가 피해
    - 일정 턴 동안 유지되며, 이후 쿨타임이 돈다.

- **스킬2 – Expose Pattern** (`HybridExposePatternSkill`)
    - 같은 대상에게 **연속 2회 이상** 공격했고, 쿨타임 0이면:
        - 이번 공격에 한해 준 피해의 10% 추가 피해
        - 이후 3턴 쿨타임
---
## 🧾 구현 기능 목록

### 1) 입력/검증

- [x] 모드 선택 (프리셋/직접 입력)
- [x] 직접 입력 모드에서 캐릭터 수 `N`을 입력받고, 2 이상인지 검증한다.
- [x] 각 캐릭터에 대해 `이름, HP, ATK, DEF, SPD, CRIT`를 파싱한다.
    - [x] HP/ATK/DEF/SPD는 양의 정수인지 검증한다.
    - [x] CRIT는 0 이상 1 이하의 실수인지 검증한다.
    - [x] 이름은 공백/빈 문자열을 허용하지 않는다.
    - [x] 이름이 중복되면 예외를 발생시킨다.
- [x] 입력 단계에서 오류가 나면 `[ERROR]` 메시지를 출력하고 **해당 단계부터 재입력** 받는다.
- [x] 프리셋 모드에서 캐릭터별 스탯/스킬 설명을 콘솔로 출력한다.

### 2) 전투 엔진

- [x] 속도 게이지 기반 1:1 턴제 전투 규칙 구현
    - [x] SPD에 따른 행동 게이지 누적
    - [x] 임계값 도달 시 행동자 결정
    - [x] 동률 시 SPD를 기준으로 행동자 결정
- [x] 기본 공격/스킬 활용 시 데미지 계산 방식을 정의한다.
    - [x] 기본 공격: `damage = ATK * (ATK / (ATK + DEF))` 형태로 계산한다.
    - [x] 치명타 확률에 따라 치명타 배율(예: 1.5배)을 적용한다.
    - [x] HP는 0 미만으로 내려가지 않도록 보정한다.
- [x] 스킬 훅 호출 순서
    - [x] `onTurnStart` → `onBeforeAttack` → 데미지 계산 → HP 적용 →  
      피격자 스킬 `onDamaged` → 공격자 스킬 `onAfterAttack`
- [x] 최대 턴 수 초과 시 무승부 처리

### 3) 프리셋 스킬 세트

- [x] `PresetCharacters.metaArchetypes()`로 6개 아키타입 기본 스탯 제공
- [x] `PresetSkillSetProvider`로 이름에 따라 올바른 스킬 세트를 바인딩
- [x] 각 스킬은 개별 클래스로 분리 (예: `AssassinOpeningStrikeSkill`, `TankHeavyArmorSkill` 등)
- [x] 브루저 분노 스택/쿨타임 상태를 별도 상태 객체(`BruiserRageState`)로 관리

### 4) 시뮬레이션/통계/밸런스 분석

- [x] `SimulationService`
    - [x] 전체 캐릭터 조합에 대해 라운드 수만큼 전투 시뮬레이션
    - [x] 캐릭터별 승/패/무/총 전투 수 집계
- [x] `CharacterStats`
    - [x] 승률, 총 전투 수, 승/패/무, 평균 턴 수 등 통계 제공
- [x] `BalanceAnalyzer`
    - [x] 승률 기준에 따라 캐릭터를  
      UNDERPOWERED / BALANCED / OVERPOWERED 로 분류  
      (기본값: 0.4 ~ 0.6 범위가 BALANCED)
    - [x] 분류 사유 메시지 생성

### 5) 뷰 레이어

- [x] `InputView`
    - [x] 모드 선택, 캐릭터 입력, 라운드 수 입력, 매트릭스 출력 여부 입력
    - [x] 프리셋 캐릭터 설명 메뉴 입력
- [x] `BalanceReportPrinter`
    - [x] 캐릭터별 승률/전투 수/분류 결과 출력
    - [x] 밸런스 기준 및 상세 사유 출력
- [x] `MatchupMatrixPrinter`
    - [x] (선택) 공격자/수비자 기준 매치업 승률 매트릭스 출력
- [x] `PresetInfoPrinter`
    - [x] 6개 메타 아키타입의 스탯/스킬 설명 출력



### 6) 리팩터링/제약 준수
- [x] 들여쓰기(depth) ≤ 2를 유지한다.
- [x] 한 메서드는 한 가지 일만 하며 15라인 이하를 지향한다.
- [x] `else`, `switch/case`, 3항 연산자 사용을 최소화하고 조기 반환을 활용한다.
- [x] 숫자/문자열 하드 코딩을 피하고, 의미 있는 `static final` 상수로 관리한다.
- [x] 도메인 규칙은 가능한 한 도메인 객체 내부에 위치시킨다.
- [x] 전투 로직과 입출력을 철저히 분리하여 테스트 가능성을 높인다.

---

## ⚠️ 예외 처리 규칙

| 상황                        | 처리 방식                                            |
|---------------------------|-----------------------------------------------------|
| 캐릭터 수가 2 미만              | `[ERROR] 캐릭터 수는 2명 이상이어야 합니다.` 후 재입력       |
| 캐릭터 스탯 파싱 실패            | `[ERROR] HP/ATK/DEF/SPD는 정수, CRIT는 실수(double)로 입력해야 합니다.` |
| HP/ATK/DEF/SPD가 1 미만     | `IllegalArgumentException` 발생 (도메인에서 검증)       |
| CRIT가 0~1 범위를 벗어남       | `IllegalArgumentException` 발생                        |
| 캐릭터 이름 중복                 | `IllegalArgumentException` 발생                        |
| 메뉴 번호 범위 밖 입력           | `[ERROR] 메뉴 번호는 N 또는 M이어야 합니다.` 후 재입력     |
| 숫자 입력에 문자열 등 잘못된 값  | `[ERROR] 정수를 입력해야 합니다.` 후 재입력                 |

- 예외는 주로 `IllegalArgumentException`으로 던지고,
- 콘솔 레벨에서는 `[ERROR] ...` 형식으로 메시지를 출력하고 재입력 받는 방식으로 처리한다.

---
## 🧪 테스트 케이스 (JUnit5 + AssertJ)
`src/test/java/balance` 이하에 JUnit5 + AssertJ 기반 테스트를 작성했다.

- `analysis/BalanceAnalyzerTest`
    - 승률에 따른 티어 분류/설명 메시지 검증

- `battle/`
    - `BattleCharacterTest` : 전투용 캐릭터 HP 처리, 사망 판정 등
    - `BattleSimulatorTest`, `BattleSimulatorCritTest` : 기본 전투 흐름, 치명타, 최대 턴 등
    - `DefaultDamageCalculatorTest` : 방어/치명타/최소 피해 규칙 검증

- `domain/CharacterTest`
    - 스탯 검증(이름 공백, 음수 스탯, CRIT 범위 등) 테스트

- `preset/PresetCharactersTest`
    - 6개 캐릭터의 기본 스탯 생성 검증

- `simulation/SimulationServiceTest`, `SimulationServiceAllTest`
    - 시뮬레이션 실행 시 통계가 기대대로 누적되는지 검증

- `skill/*SkillsTest`
    - 각 캐릭터별 스킬 효과(추가 피해, 회복, 쿨타임, 스택 로직 등)를 개별적으로 검증

- `view/InputViewParseTest`
    - `parseCharacterLine`의 정상/에러 입력 처리 검증 
> 테스트에서는 대부분 **`FixedRandomProvider`**를 주입해 난수에 의존하지 않는 **결정적 시나리오**를 구성한다.

---
## 🧱 프로젝트 구조

```plaintext
src/main/java/balance
├─ Application.java                # 콘솔 진입점
├─ analysis/
│   ├─ BalanceAnalyzer.java        # 승률 기반 밸런스 평가 로직
│   ├─ BalanceAssessment.java      # 캐릭터별 평가 결과 DTO
│   └─ BalanceTier.java            # BALANCED / NEEDS_BUFF / NEEDS_NERF 등
├─ battle/
│   ├─ BattleSimulator.java        # 속도 게이지 기반 1:1 전투 엔진
│   ├─ BattleResult.java           # 전투 결과 (승자/패자/턴 수/승자 남은 HP 비율)
│   ├─ DamageCalculator.java       # 데미지 계산 전략 인터페이스
│   └─ DefaultDamageCalculator.java
├─ domain/
│   └─ Character.java              # 기본 캐릭터 스펙 (이름, HP, ATK, DEF, SPD, CRIT)
├─ preset/
│   └─ PresetCharacters.java       # 6개 메타 아키타입 캐릭터 정의
├─ scenario/
│   └─ PresetScenarios.java        # (확장용) 다른 시나리오용 프리셋 묶음
├─ simulation/
│   ├─ SimulationService.java      # 전체 리그/매트릭스 시뮬레이션
│   ├─ CharacterStats.java         # 캐릭터별 통계 집계
│   └─ MatchupMatrix.java          # 캐릭터 간 승률 매트릭스
├─ skill/
│   ├─ Skill.java                  # 스킬 훅 인터페이스
│   ├─ SkillContext.java           # 스킬 컨텍스트 (self/opponent/플래그 등)
│   ├─ SkillSetProvider.java       # 캐릭터별 스킬 세트 제공자 인터페이스
│   ├─ PresetSkillSetProvider.java # 프리셋 캐릭터용 스킬 세트 매핑
│   ├─ assassin/                   # Assassin 관련 스킬 3종
│   ├─ bruiser/                    # Bruiser 관련 스킬 3종 + RageState
│   ├─ hybrid/                     # Hybrid 관련 스킬 3종
│   ├─ speedster/                  # Speedster 관련 스킬 3종
│   ├─ sustainer/                  # Sustainer 관련 스킬 3종
│   └─ tank/                       # Tank 관련 스킬 3종
├─ support/
│   ├─ RandomProvider.java         # 난수 전략 인터페이스
│   ├─ DefaultRandomProvider.java  # java.util.Random 기반 구현
│   └─ FixedRandomProvider.java    # 테스트용 고정 난수 공급자
└─ view/
    ├─ InputView.java              # 콘솔 입력 처리
    ├─ BalanceReportPrinter.java   # 전체 밸런스 리포트 출력
    ├─ MatchupMatrixPrinter.java   # 매치업 승률 매트릭스 출력
    └─ PresetInfoPrinter.java      # 프리셋 캐릭터 스탯/스킬 설명 출력
```
---

## 🧾 커밋 컨벤션 (AngularJS Style)

프로젝트에서는 다음과 같은 커밋 타입을 사용한다.

| 타입              | 설명                          | 예시                                                  |
| ----------------- | ----------------------------- | ----------------------------------------------------- |
| `docs:`           | 문서/README 수정              | `docs: README에 전투 규칙 및 프리셋 설명 추가`        |
| `feat(domain):`   | 도메인/전투/스킬 로직 추가    | `feat(domain): Assassin 스킬 3종 및 훅 연결`         |
| `feat(simulation):` | 시뮬레이션/통계 로직 추가   | `feat(simulation): 전체 리그 시뮬레이션 및 통계 집계` |
| `feat(view):`     | 콘솔 입출력/뷰 로직 추가      | `feat(view): 매치업 승률 매트릭스 출력 기능 추가`    |
| `test:`           | 테스트 코드 추가/수정         | `test: Bruiser/Assassin 스킬 단위 테스트 추가`       |
| `refactor:`       | 리팩터링 (기능 변화 없음)     | `refactor: BattleSimulator 내부 메서드 분리`          |
| `fix:`            | 버그 수정                     | `fix: 승리 시 남은 HP 비율 계산 오류 수정`           |
---

## 💡 개발 진행 방식
1) **README 기능 목록 작성** 
   - 구현할 기능을 모두 나열하고, 필수/심화 기능을 구분한다.
2) **도메인부터 TDD로 시작**
    - Character, Skill, BattleResult, SimulationConfig부터 테스트 → 구현 → 리팩터링 순으로 진행한다.
3) **전투 엔진 구현**
    - 매우 작은 규칙(기본 공격만)으로 시작해서, 치명타 → 스킬 → 상태이상 순으로 점진적으로 확장한다.
4) **시뮬레이션 + 통계 집계 구현**
    - 두 캐릭터만 대상으로 테스트 → 여러 캐릭터 확장 → 전체 매치업 확장 순으로 진행한다.
5) **밸런스 분석 로직 구현**
   - 승률 기준 정의 → 상태 분류 → 지수 계산 → 리포트 객체로 묶는다.
6) **콘솔 입출력 연결**
   - 도메인/서비스가 안정되면 InputView/OutputView를 붙여 실제 흐름을 완성한다.
7) **리팩터링 및 문서화**
   - 메서드 길이/indent/하드 코딩/네이밍 등을 점검하며 구조를 다듬고, “어떤 기준으로 밸런스를 정의했는지, 어떤 시행착오가 있었는지”를 문서로 남긴다.
---
## 🔧 확장 가능성

- **새 캐릭터/스킬 추가**
    - `PresetCharacters`에 캐릭터 스펙 추가
    - `PresetSkillSetProvider`에 해당 캐릭터 이름 → 스킬 세트 매핑 추가
    - 필요 시 `PresetInfoPrinter`에 설명 추가

- **전투 규칙 조정**
    - `DefaultDamageCalculator`에서 방어 계수, 치명타 배수, 최소 데미지 정책 변경
    - `BattleSimulator`의 행동 게이지 로직(SPD 가중치, 최대 턴 수 등) 변경

- **밸런스 기준 커스터마이징**
    - `BalanceAnalyzer`에 승률 기준(40~60%)을 외부에서 주입 가능하도록 변경
    - 승률 분산, 표준편차 등을 이용한 “밸런스 지수” 확장

- **UI/웹 확장**
    - 현재 도메인/전투/시뮬레이션/분석 로직은 순수 Java로 작성되어 있어,
      추후 Spring Boot REST API나 웹 UI에서 재사용 가능하다.

---
## ⚙️ 개발 환경
- 언어: Java 21
- 빌드 도구: Gradle
- 테스트: JUnit 5 + AssertJ
- IDE: IntelliJ IDEA
---
## 📚 참고 자료
- JUnit 5 User Guide
- AssertJ User Guide / Exception Assertions
- Guide to JUnit 5 Parameterized Tests
- 우아한테크코스 Java Style Guide
- Angular Commit Message Conventions