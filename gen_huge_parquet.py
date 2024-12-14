import pandas as pd
import pyarrow as pa
from pyarrow import parquet
import random


schema = pa.schema([
    ('name', pa.string()),
    ('coordinates_x', pa.float64()),
    ('coordinates_y', pa.float64()),
    ('eyeColor', pa.string()),
    ('hairColor', pa.string()),
    ('location_x', pa.float64()),
    ('location_y', pa.float64()),
    ('location_z', pa.float64()),
    ('height', pa.float64()),
    ('birthday', pa.string()),
    ('nationality', pa.string())
])

size = 100

data = {
    'name': [f"test {i}" for i in range(size)],
    'coordinates_x': [1] * size,
    'coordinates_y': [1] * size,
    'eyeColor': [random.choice(['BLACK', 'BLUE', 'ORANGE']) for _ in range(size)],
    'hairColor': [random.choice(['BLACK', 'BLUE', 'ORANGE']) for _ in range(size)],
    'location_x': [1] * size,
    'location_y': [1] * size,
    'location_z': [1] * size,
    'height': [random.randint(100, 200) for _ in range(size)],
    'birthday': ['1990-01-01T00:00:00Z'] * size,
    'nationality': [random.choice(['RUSSIA', 'FRANCE', 'ITALY', 'GERMANY']) for _ in range(size)]
}

df = pd.DataFrame(data)
table = pa.Table.from_pandas(df, schema=schema)

parquet.write_table(
    table, 
    'big_persons.parquet',
    compression='snappy',
    version='2.6'
)
