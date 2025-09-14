INSERT INTO tasks (title, due_date, created_by, status, priority, updated)
VALUES
  ('Configurar cobertura de pruebas',      TIMESTAMP '2025-09-20 18:00:00', 1, 'PENDING',     'HIGH',   FALSE),
  ('Escribir pruebas unitarias del servicio', TIMESTAMP '2025-09-25 12:00:00', 2, 'IN_PROGRESS','MEDIUM', TRUE);