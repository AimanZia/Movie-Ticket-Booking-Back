import requests
import sys

userServiceURL = "http://localhost:8080"
bookingServiceURL = "http://localhost:8081"
walletServiceURL = "http://localhost:8082"

def main():
    name = "John Doe"
    email = "johndoe@mail.com"
    showID = 7
    return add_money_and_book_ticket(name, email, showID)

def create_user(name, email):
    new_user = {"name": name, "email": email}
    response = requests.post(userServiceURL + "/users", json=new_user)
    return response

def create_wallet(user_id):
    requests.put(walletServiceURL+f"/wallets/{user_id}", json={"action":"credit", "amount":0})

def get_wallet(user_id):
    response = requests.get(walletServiceURL + f"/wallets/{user_id}")
    return response

def update_wallet(user_id, action, amount):
    response = requests.put(walletServiceURL + f"/wallets/{user_id}", json={"action":action, "amount":amount})
    return response

def get_show_details(show_id):
    response = requests.get(bookingServiceURL + f"/shows/{show_id}")
    return response   


def delete_show(user_id,show_id):
    response = requests.delete(bookingServiceURL+f"/bookings/users/{user_id}/shows/{show_id}")
    return response

def delete_users():
    requests.delete(userServiceURL+f"/users")

def delete_wallets():
    requests.delete(walletServiceURL+f"/wallets")

def add_money_and_book_ticket(name,email,showID):
    try:
        delete_users()
        delete_wallets()
        new_user = create_user(name,email) #create_user
        new_userid = new_user.json()['id']
        update_wallet(new_userid,"credit",1000) #update_wallet
        show_details_before_booking = get_show_details(showID) #get_show_details
        old_wallet_balance = get_wallet(new_userid).json()['balance'] #get_wallet
        new_booking = {"show_id": showID, "user_id": new_userid, "seats_booked": 10}
        requests.post(bookingServiceURL + "/bookings", json=new_booking)
        delete_show(new_userid,showID) #delete_show
        show_details_after_booking = get_show_details(showID)
        if(show_details_after_booking.json()['seats_available'] == show_details_before_booking.json()['seats_available'] and old_wallet_balance == get_wallet(new_userid).json()['balance']):
            print("Test passed")
            return True
        else:
            print("Test failed")
            return False
    except:
        print("Some Exception Occurred")
        return False

if __name__ == "__main__":
    if main():
        sys.exit(0)
    else:
        sys.exit(1)
