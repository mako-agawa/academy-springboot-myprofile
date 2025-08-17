CREATE TABLE learning_data (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(25) NOT NULL,
  time_record INT,
  learning_date TIMESTAMP,
  user_id BIGINT,
  category_id BIGINT,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO learning_data (title, time_record, learning_date, user_id, category_id, created_at, updated_at)
VALUES
  ('Spring Boot基礎', 120, NOW(), 1, 1, NOW(), NOW()),
  ('React入門', 90, NOW(), 2, 2, NOW(), NOW()),
  ('Dockerハンズオン', 60, NOW(), 3, 3, NOW(), NOW()),
  ('Java演習', 150, NOW(), 4, 1, NOW(), NOW()),
  ('TypeScript応用', 80, NOW(), 1, 2, NOW(), NOW()),
  ('AWSネットワーク基礎', 110, NOW(), 2, 3, NOW(), NOW()),
  ('Spring Security', 100, NOW(), 3, 1, NOW(), NOW()),
  ('Next.js練習', 75, NOW(), 4, 2, NOW(), NOW()),
  ('Linux基礎コマンド', 50, NOW(), 1, 3, NOW(), NOW()),
  ('JPA/Hibernate', 130, NOW(), 2, 1, NOW(), NOW()),
  ('CSS設計BEM', 60, NOW(), 3, 2, NOW(), NOW()),
  ('Kubernetes基礎', 140, NOW(), 4, 3, NOW(), NOW()),
  ('Java Streams API', 90, NOW(), 1, 1, NOW(), NOW()),
  ('Tailwind CSS', 70, NOW(), 2, 2, NOW(), NOW()),
  ('Terraform入門', 100, NOW(), 3, 3, NOW(), NOW()),
  ('Spring REST API', 120, NOW(), 4, 1, NOW(), NOW()),
  ('Vue.js比較学習', 80, NOW(), 1, 2, NOW(), NOW()),
  ('Nginx設定演習', 60, NOW(), 2, 3, NOW(), NOW()),
  ('Javaマルチスレッド', 150, NOW(), 3, 1, NOW(), NOW()),
  ('UIコンポーネント設計', 90, NOW(), 4, 2, NOW(), NOW());