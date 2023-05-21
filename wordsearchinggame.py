import tkinter as tk
import random

words = ['apple', 'banana', 'cherry', 'grape', 'orange', 'kiwi', 'mango', 'pear', 'pineapple', 'strawberry']

class WordSearchGame:
    def __init__(self, root):
        self.root = root
        self.root.title("Word Search Game")
        self.word_to_find = random.choice(words)
        self.found_words = []
        
        self.word_label = tk.Label(self.root, text=f"Find the word: {self.word_to_find}", font=("Arial", 16))
        self.word_label.pack(pady=10)
        
        self.canvas = tk.Canvas(self.root, width=400, height=400, borderwidth=2, relief="solid")
        self.canvas.pack()
        
        self.canvas.bind("<Button-1>", self.check_word)
        self.canvas.bind("<B1-Motion>", self.check_word)
        
        self.draw_grid()
        self.place_word()
    
    def draw_grid(self):
        for i in range(10):
            self.canvas.create_line(i * 40, 0, i * 40, 400)
            self.canvas.create_line(0, i * 40, 400, i * 40)
    
    def place_word(self):
        start_row = random.randint(0, 10 - len(self.word_to_find))
        start_col = random.randint(0, 10 - len(self.word_to_find))
        for i, letter in enumerate(self.word_to_find):
            x = (start_col + i) * 40 + 20
            y = (start_row + i) * 40 + 20
            self.canvas.create_text(x, y, text=letter, font=("Arial", 16), tags="word")
    
    def check_word(self, event):
        item = self.canvas.find_closest(event.x, event.y)[0]
        tags = self.canvas.gettags(item)
        
        if "word" in tags:
            x, y = self.canvas.coords(item)
            row = int((y - 20) / 40)
            col = int((x - 20) / 40)
            
            word = ""
            for i in range(min(10 - row, 10 - col)):
                item = self.canvas.find_closest((col + i) * 40 + 20, (row + i) * 40 + 20)[0]
                tags = self.canvas.gettags(item)
                if "word" in tags:
                    word += self.canvas.itemcget(item, 'text')
            
            if word == self.word_to_find:
                if word not in self.found_words:
                    self.found_words.append(word)
                    self.canvas.itemconfig("word", fill="green")
                    self.word_label.config(text=f"Find the word: {self.word_to_find} (Found {len(self.found_words)} words)")
                    if len(self.found_words) == len(words):
                        self.word_label.config(text="Congratulations! You found all the words!")
            else:
                self.canvas.itemconfig("word", fill="black")
    
if __name__ == "__main__":
    root = tk.Tk()
    game = WordSearchGame(root)
    root.mainloop()
