DELETE FROM result;
DELETE FROM player;
DELETE FROM event;
DELETE FROM team;
DELETE FROM stage;
DELETE FROM competition;
DELETE FROM sport;
DELETE FROM venue;

INSERT INTO sport (sport_id, name) VALUES (1, 'Football');
INSERT INTO sport (sport_id, name) VALUES (2, 'Ice Hockey');

INSERT INTO competition (competition_id, origin_competition_id, name, _sport_id)
VALUES (1, 'AUT_BL_1', 'Austrian Bundesliga', 1);
INSERT INTO competition (competition_id, origin_competition_id, name, _sport_id)
VALUES (2, 'AUT_EBEL_1', 'ICE Hockey League', 2);

INSERT INTO stage (stage_id, name, ordering, _competition_id)
VALUES (1, 'Regular Season', 1, 1);
INSERT INTO stage (stage_id, name, ordering, _competition_id)
VALUES (2, 'Playoffs', 2, 2);

INSERT INTO venue (venue_id, name, city) VALUES (1, 'Red Bull Arena', 'Salzburg');
INSERT INTO venue (venue_id, name, city) VALUES (2, 'Merkur-Arena', 'Graz');
INSERT INTO venue (venue_id, name, city) VALUES (3, 'Stadthalle', 'Klagenfurt');

INSERT INTO team (team_id, name, official_name, slug, abbreviation, team_country_code) VALUES
(1, 'Salzburg', 'FC Red Bull Salzburg', 'salzburg', 'RBS', 'AUT'),
(2, 'Sturm Graz', 'SK Sturm Graz', 'sturm-graz', 'STU', 'AUT'),
(3, 'KAC', 'EC KAC', 'kac', 'KAC', 'AUT'),
(4, 'Capitals', 'Vienna Capitals', 'capitals', 'VIC', 'AUT'),
(5, 'Bayern', 'FC Bayern München', 'bayern-munchen', 'FCB', 'GER');

INSERT INTO event (event_id, season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (1, 2026, 'finished', '2026-03-10', '17:00:00', 1, 2, 1, 1);

INSERT INTO event (event_id, season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (2, 2026, 'finished', '2026-03-12', '19:15:00', 3, 4, 2, 3);

INSERT INTO event (event_id, season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (3, 2026, 'scheduled', '2026-05-20', '20:45:00', 5, 1, 1, 1);

INSERT INTO event (event_id, season, status, date_venue, time_venue_utc, _home_team_id, _away_team_id, _stage_id, _venue_id)
VALUES (4, 2026, 'scheduled', '2026-06-15', '18:00:00', 2, 5, 1, 2);

INSERT INTO result (result_id, _event_id, home_goals, away_goals, _winner_team_id, message)
VALUES (1, 1, 3, 1, 1, 'Dominant performance by Salzburg');

INSERT INTO result (result_id, _event_id, home_goals, away_goals, _winner_team_id, message)
VALUES (2, 2, 4, 4, NULL, 'Thrilling draw in Klagenfurt');

SELECT setval('sport_sport_id_seq', (SELECT MAX(sport_id) FROM sport));
SELECT setval('team_team_id_seq', (SELECT MAX(team_id) FROM team));
SELECT setval('event_event_id_seq', (SELECT MAX(event_id) FROM event));
SELECT setval('result_result_id_seq', (SELECT MAX(result_id) FROM result));