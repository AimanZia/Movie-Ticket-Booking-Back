import requests
import sys
from utils import *
from user import *
from wallet import *
from booking import *

# Get a non-existant User

def main():
    response = delete_users()
    if not check_response_status_code(response, 200):
        return False
    
    user_id = 1
    
    response = get_user(user_id)
    if not check_response_status_code(response, 404):
        return False

    return True

if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
