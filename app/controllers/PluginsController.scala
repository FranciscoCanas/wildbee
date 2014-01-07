package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Plugins
import models.NewPlugin
import models.Packages
import helpers.ObserverHelper

object PluginsController extends Controller {

  val pluginForm = Form(
  mapping(
    "name"   -> nonEmptyText,
    "pack"   -> nonEmptyText
  )(NewPlugin.apply)(NewPlugin.unapply))

  def newPlugin = Action { implicit request =>
    Ok(views.html.plugins.newEntity(pluginForm))
  }

  def index() = Action { implicit request =>
    Ok(views.html.plugins.index())
  }

  def create() = Action { implicit request =>
    pluginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.plugins.newEntity(formWithErrors)),
      plugin => {
        Plugins.insert(newInstance = plugin)
        Redirect(routes.PluginsController.index())
      })
  }

  def show(plugin: String) = Action { implicit request =>
    Ok(views.html.plugins.show(Plugins.find(plugin)))
  }

  def edit(id: String) = Action { implicit request =>
    val plugin = Plugins.mapToNew(Plugins.uuid(id))
    val filledForm = pluginForm.fill(plugin)
    Ok(views.html.plugins.edit(filledForm, id))
  }
  def update(id: String) = Action { implicit request =>
    val oldPlugin = Plugins.find(id)
      pluginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.plugins.edit(formWithErrors, id)),
        updatedPlugin => {
          Plugins.update(Plugins.mapToEntity(oldPlugin.id, updatedPlugin))
          Redirect(routes.PluginsController.show(id))
            .flashing("success" -> "Package Updated!")
        }
      )
  }


  def delete(plugin: String) = Action { implicit request =>
    println("PLUGIN NAME IS: " + plugin)
    val uuid = Plugins.nameToId(plugin)
    Plugins.delete(uuid)
    Redirect(routes.PluginsController.index)
  }



  def findPlugins: Map[String, String] = {
    ObserverHelper.mapIdToName
  }
}