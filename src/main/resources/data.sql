-- 1. Sports
INSERT INTO sport (name) VALUES ('Football'), ('Ice Hockey'), ('Basketball');

-- 2. Teams
INSERT INTO team (name, official_name, slug, abbreviation, team_country_code) VALUES
('Salzburg', 'FC Red Bull Salzburg', 'salzburg', 'RBS', 'AUT'),
('Sturm Graz', 'SK Sturm Graz', 'sturm-graz', 'STU', 'AUT'),
('KAC', 'EC KAC', 'kac', 'KAC', 'AUT'),
('Capitals', 'Vienna Capitals', 'capitals', 'VIC', 'AUT'),
('Bayern', 'FC Bayern München', 'bayern-munchen', 'FCB', 'GER');

-- 3. Competitions
INSERT INTO competition (origin_competition_id, name, _sport_id) VALUES
('AUT_BL_1', 'Austrian Bundesliga', 1),
('AUT_EBEL_1', 'ICE Hockey League', 2);

-- 4. Stages
INSERT INTO stage (name, ordering, _competition_id) VALUES
('Regular Season', 1, 1),
('Playoffs', 2, 2);

-- 5. Venues
INSERT INTO venue (name, city) VALUES
('Red Bull Arena', 'Salzburg'),
('Merkur-Arena', 'Graz'),
('Stadthalle', 'Klagenfurt');

-- 6. Events (Matches)
-- Football: Salzburg vs Sturm
INSERT INTO event (season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (2026, 'finished', '2026-03-15', '18:30:00', 1, 2, 1, 1);

-- Hockey: KAC vs Capitals
INSERT INTO event (season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (2026, 'scheduled', '2026-10-23', '09:45:00', 3, 4, 2, 3);

-- Live Match: Bayern vs Salzburg
INSERT INTO event (season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (2026, 'live', CURRENT_DATE, '20:00:00', 5, 1, 1, 1);

-- 7. Results (Tylko dla zakończonych meczów)
INSERT INTO result (_event_id, home_goals, away_goals, _winner_team_id, message)
VALUES (1, 2, 1, 1, 'Great victory for Salzburg');

-- 8. Players (Przykładowi gracze do drużyn)
INSERT INTO player (name, position, _team_id) VALUES
('John Striker', 'Forward', 1),
('Marc Defense', 'Defender', 2),
('Ice Goalie', 'Goalkeeper', 3);