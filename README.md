[![N|Solid](https://i.imgur.com/8YoKlal.png)](https://arrav.net/)
# Emulating Something Remarkable.
___
> As Arrav came across the fields to the south of Avarrocka he saw over the trees a column of black smoke. It was a windless day, and this column - climbing straight and true into the sky - looked to Arrav like the finger of an angry god. He hastened his pace, racing through the forests beyond which lay Avarrocka. A few beasts lunged at him from the surrounding vegetation, but he paid them no heed, swatting claws and teeth back with swift flicks of his powerful hands.

### Server's Core & Stability
Countless months of work brought perfection to our core. Taken into account every improvement to create to totally best we could provide. Here is a brief list of core improvements and system we created to make Arrav truly capable to deliver its potential.
  - Latest Netty networking system (4.1.19). Delivering a 'one handler' per session pipeline that maximalizes performance. Queued incoming/outgoing packets. There is also one channel allocated buffer stream per session to which we write the outgoing packets. No overheading and memory-effective networking system.
  - Thread-safe queued logins and logouts to ensure the world integrity.
  - Task driven server which ensures structured and well procuded content.
  - Memory-mapped cache system which decodes defintions at a fast speed.
  - Thread-safe parrallel synchronizer in charge of player updating.
  - Action triggers for short interactions used by incoming packets. (considered to be a plugin system). Thus avoiding looping over countless ids to determine a button click.
  - A unique pathfinding system which supports different algorithms including an optimized version of A* and a straight line caster. All of this is backed by the traversable map created by decoding the whole world map.
  - Game objects (those physical ones such as walls and desks) are processed under two modes: Static & Dynamic. Static objects emphasizes on memory effective methods and are used mostly for player interaction validation. Dynamic objects are those who are created during runtime and hold more attributes. Both states have full support on thread-safe registering/unregistering.
  - Regional based system which handles ground items, game objects and actors(players and mobs) dynamically during game play. This limits iterations used by various content-written features.
  - SQL Pooling system which works apart from the game-thread.

Server was tested in various conditions. All of the results were taken seriously to improve further performance. Request these individually for elaboration on this subject.

### Content & Features
All content was written to be minimal on the cycle time. Trying to avoid iterations and provide the best outcome possible. By using different design techniques, the content is written with the object-oriented-language Java. All this content relates to the official game.

All parentheses will signify code design and improvements (written this way).
  - Achievement system with difficulty (which provides stages without achievement repetition)
  - Clan chat system with management commands (single line widget sent)
  - Game commands (with java signature annotations)
  - Dialogue system with several dialogue types and face expressions (supports easy dialogue chaining outside of declarations)
  - Flexible pets following system with feeding and growing

  All other content and features are to be requested specifically.
