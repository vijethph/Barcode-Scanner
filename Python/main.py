# Kivy OpenCV Barcode Scanner
# done by Vijeth P H
from kivy.app import App
from kivy.uix.image import Image
from kivy.uix.widget import Widget
from kivy.uix.label import Label
from kivy.uix.button import Button
from kivy.uix.togglebutton import ToggleButton
from kivy.uix.boxlayout import BoxLayout
from kivy.clock import Clock
from kivy.graphics.texture import Texture
from pyzbar import pyzbar
import cv2

# Create global variables, for storing and displaying barcodes
outputtext=''
leb=Label(text=outputtext,size_hint_y=None,height='48dp',font_size='45dp')
found = set()       # this will not allow duplicate barcode scans to be stored
togglflag=True

class BarcodeApp(App):
    # initialize all widgets for display
    def build(self):
        layout=BoxLayout()      # boxlayout is where widgets are placed adjacent to each other
        layout.orientation='vertical'      # make adjacency of widgets vertical
        # create OpenCV camera
        self.cam=cv2.VideoCapture(0)
        self.cam.set(3,1280)        # set resolution of camera
        self.cam.set(4,720)
        self.img=Image()
        
        # create Toggle Button for pause and play of video stream
        self.togbut=ToggleButton(text='Pause',group='camstart',state='down',size_hint_y=None,height='48dp',on_press=self.change_state)
        self.but=Button(text='Stop',size_hint_y=None,height='48dp',on_press=self.stop_stream)
        layout.add_widget(self.img)
        layout.add_widget(leb)
        layout.add_widget(self.togbut)
        layout.add_widget(self.but)
        Clock.schedule_interval(self.update,1.0/30)     # update for 30fps
        
        return layout
        
    # update frame of OpenCV camera
    def update(self,dt):
        if togglflag:
            ret, frame = self.cam.read()    # retrieve frames from OpenCV camera
            
            if ret:
                buf1=cv2.flip(frame,0)      # convert it into texture 
                buf=buf1.tostring()
                image_texture=Texture.create(size=(frame.shape[1],frame.shape[0]),colorfmt='bgr')
                image_texture.blit_buffer(buf,colorfmt='bgr',bufferfmt='ubyte')
                self.img.texture=image_texture  # display image from the texture
                
                barcodes = pyzbar.decode(frame)     # detect barcode from image
                for barcode in barcodes:
                    (x, y, w, h) = barcode.rect
                    cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
                    barcodeData = barcode.data.decode("utf-8")
                    barcodeType = barcode.type
                    text = "{} ({})".format(barcodeData, barcodeType)
                    cv2.putText(frame, text, (x, y - 10),cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
                    if barcodeData not in found:    # check if detected barcode is a duplicate
                        outputtext=text
                        leb.text=outputtext         # display the barcode details
                        found.add(barcodeData)
                
                key = cv2.waitKey(1) & 0xFF
                if key == ord("q"):
                    pass
        
    # change state of toggle button
    def change_state(self,*args):
        global togglflag
        if togglflag:
            self.togbut.text='Play'
            togglflag=False
        else:
            self.togbut.text='Pause'
            togglflag=True
            
            
    def stop_stream(self,*args):
        self.cam.release()
            
            
if __name__ == '__main__':
    BarcodeApp().run()
    cv2.destroyAllWindows()
