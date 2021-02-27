# Sweet Swipe

Sweet Swipe est une application android présentant à l'utilisateur un ensemble de profil avec une photo et une description. 
En double-cliquant sur une photo de profil l'utilisateur peut "liker" le profil.

## Animations

Toutes les transitions sont animées par des animations android customisées à partir de la classe *Animation*.  
L'application implémentant la détection des mouvements "swipe" pour afficher le profil suivant ou précédent: . 

<img src="readme_src/swipe_photo_profil.gif" width="300">

Afficher ou masquer la description du profil:

<img src="readme_src/swipe_details_profil.gif" width="300">

La détection du double-clique afin de "liker" un profil:

<img src="readme_src/envoyer_amour.gif" width="300">

## Swap buffer pour la transition entre les images de profil

L'effet de transition entre les photos de profil est réalisé via un swap buffer composé de deux ImageView.

<img src="readme_src/swap_buffer_schema.jpg" width="1000">

