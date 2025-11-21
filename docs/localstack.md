commands to get localstack running for AWS testing:

# 1. Create a virtual environment
python3 -m venv ~/.localstack-venv

# 2. Activate it
source ~/.localstack-venv/bin/activate

# 3. Upgrade pip
pip install --upgrade pip

# 4. Install LocalStack
pip install localstack

# 5. Run LocalStack
localstack start
