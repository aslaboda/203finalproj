/*
Super class Action
*/

abstract class Action
{
   // Fields:
   protected MovingEntity entity;

   // Constructor:
   protected Action(MovingEntity entity)
   {
      this.entity = entity;
   }

   // Abstract Methods:
   protected abstract void executeAction(EventScheduler eventScheduler);

}
